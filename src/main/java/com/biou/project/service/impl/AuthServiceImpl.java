package com.biou.project.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.biou.project.dto.DingTalkLoginDTO;
import com.biou.project.dto.LoginDTO;
import com.biou.project.dto.TokenRefreshDTO;
import com.biou.project.entity.User;
import com.biou.project.exception.BusinessException;
import com.biou.project.service.AuthService;
import com.biou.project.service.UserService;
import com.biou.project.util.DingTalkUtils;
import com.biou.project.util.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import com.biou.project.vo.DingTalkLoginUrlVO;
import com.biou.project.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * 
 * @author Jax
 * @since 2024-01-01
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DingTalkUtils dingTalkUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:86400}")
    private Long jwtExpiration;

    private static final String DINGTALK_STATE_PREFIX = "dingtalk:state:";
    private static final String JWT_BLACKLIST_PREFIX = "jwt:blacklist:";

    @Override
    public LoginVO login(LoginDTO loginDTO, String clientIp) {
        User user = userService.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        updateUserLoginInfo(user, clientIp);

        return generateLoginResponse(user);
    }

    @Override
    public DingTalkLoginUrlVO getDingTalkLoginUrl() {
        String state = UUID.randomUUID().toString().replace("-", "");
        String loginUrl = dingTalkUtils.getLoginUrl(state);

        redisTemplate.opsForValue().set(DINGTALK_STATE_PREFIX + state, "valid", 300, TimeUnit.SECONDS);

        return new DingTalkLoginUrlVO(loginUrl, state);
    }

    @Override
    public LoginVO dingTalkLogin(DingTalkLoginDTO dingTalkLoginDTO, String clientIp) {
        if (!redisTemplate.hasKey(DINGTALK_STATE_PREFIX + dingTalkLoginDTO.getState())) {
            throw new BusinessException("无效的登录状态，请重新获取登录链接");
        }

        try {
            JSONObject userInfo = dingTalkUtils.getUserInfoByCode(dingTalkLoginDTO.getCode());
            String unionId = userInfo.getString("unionid");
            String nick = userInfo.getString("nick");
            String openId = userInfo.getString("openid");

            User user = userService.findByDingtalkUnionId(unionId);
            if (user == null) {
                user = createDingTalkUser(unionId, nick, openId);
            }

            if (user.getStatus() == 0) {
                throw new BusinessException("账户已被禁用");
            }

            updateUserLoginInfo(user, clientIp);

            redisTemplate.delete(DINGTALK_STATE_PREFIX + dingTalkLoginDTO.getState());

            return generateLoginResponse(user);

        } catch (Exception e) {
            throw new BusinessException("钉钉登录失败：" + e.getMessage());
        }
    }

    @Override
    public LoginVO refreshToken(TokenRefreshDTO tokenRefreshDTO) {
        try {
            String newAccessToken = jwtUtils.refreshAccessToken(tokenRefreshDTO.getRefreshToken());
            Long userId = jwtUtils.getUserIdFromToken(newAccessToken);
            User user = userService.getById(userId);

            if (user == null || user.getStatus() == 0) {
                throw new BusinessException("用户不存在或已被禁用");
            }

            LoginVO loginVO = new LoginVO();
            loginVO.setAccessToken(newAccessToken);
            loginVO.setRefreshToken(tokenRefreshDTO.getRefreshToken());
            loginVO.setExpiresIn(jwtExpiration);
            loginVO.setUserInfo(buildUserInfoVO(user));

            return loginVO;

        } catch (Exception e) {
            throw new BusinessException("Token刷新失败：" + e.getMessage());
        }
    }

    @Override
    public void logout(String accessToken) {
        try {
            Long userId = jwtUtils.getUserIdFromToken(accessToken);
            long expiration = jwtUtils.getExpirationDateFromToken(accessToken).getTime() - System.currentTimeMillis();
            
            if (expiration > 0) {
                redisTemplate.opsForValue().set(JWT_BLACKLIST_PREFIX + accessToken, userId.toString(), 
                              expiration, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            // 忽略无效token的注销请求
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (redisTemplate.hasKey(JWT_BLACKLIST_PREFIX + token)) {
                return false;
            }
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    private User createDingTalkUser(String unionId, String nick, String openId) {
        User user = new User();
        user.setUsername(generateUniqueUsername(nick));
        user.setRealName(nick);
        user.setDingtalkUnionId(unionId);
        user.setDingtalkUserId(openId);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);

        userService.save(user);
        return user;
    }

    private String generateUniqueUsername(String nick) {
        String baseUsername = "dingtalk_" + nick.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");
        String username = baseUsername;
        int suffix = 1;

        while (userService.findByUsername(username) != null) {
            username = baseUsername + "_" + suffix++;
        }

        return username;
    }

    private void updateUserLoginInfo(User user, String clientIp) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        userService.updateById(user);
    }

    private LoginVO generateLoginResponse(User user) {
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId(), user.getUsername());

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(jwtExpiration);
        loginVO.setUserInfo(buildUserInfoVO(user));

        return loginVO;
    }

    private LoginVO.UserInfoVO buildUserInfoVO(User user) {
        LoginVO.UserInfoVO userInfoVO = new LoginVO.UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setRealName(user.getRealName());
        return userInfoVO;
    }
}
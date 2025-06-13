package com.biou.project.service;

import com.biou.project.dto.DingTalkLoginDTO;
import com.biou.project.dto.LoginDTO;
import com.biou.project.dto.TokenRefreshDTO;
import com.biou.project.vo.DingTalkLoginUrlVO;
import com.biou.project.vo.LoginVO;

/**
 * 认证服务接口
 * 
 * @author Jax
 * @since 2025-06-14
 */
public interface AuthService {

    /**
     * 用户名密码登录
     *
     * @param loginDTO 登录信息
     * @param clientIp 客户端IP
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO, String clientIp);

    /**
     * 获取钉钉登录URL
     *
     * @return 钉钉登录URL信息
     */
    DingTalkLoginUrlVO getDingTalkLoginUrl();

    /**
     * 钉钉扫码登录
     *
     * @param dingTalkLoginDTO 钉钉登录信息
     * @param clientIp 客户端IP
     * @return 登录结果
     */
    LoginVO dingTalkLogin(DingTalkLoginDTO dingTalkLoginDTO, String clientIp);

    /**
     * 刷新访问token
     *
     * @param tokenRefreshDTO 刷新token信息
     * @return 新的token信息
     */
    LoginVO refreshToken(TokenRefreshDTO tokenRefreshDTO);

    /**
     * 用户登出
     *
     * @param accessToken 访问token
     */
    void logout(String accessToken);

    /**
     * 验证token是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    boolean validateToken(String token);
}
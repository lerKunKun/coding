package com.biou.project.controller;

import com.biou.annotation.AuditLog;
import com.biou.project.dto.DingTalkLoginDTO;
import com.biou.project.dto.LoginDTO;
import com.biou.project.dto.TokenRefreshDTO;
import com.biou.project.service.AuthService;
import com.biou.project.vo.DingTalkLoginUrlVO;
import com.biou.project.vo.LoginVO;
import com.biou.project.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 认证控制器
 * 
 * @author Jax
 * @since 2025-06-14
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * 用户名密码登录
     *
     * @param loginDTO 登录信息
     * @param request HTTP请求
     * @return 登录结果
     */
    @PostMapping("/login")
    @AuditLog(operationType = "LOGIN", description = "用户登录")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        logger.info("用户登录请求: {}", loginDTO);
        
        String clientIp = getClientIp(request);
        LoginVO loginVO = authService.login(loginDTO, clientIp);
        
        logger.info("用户登录成功: {}", loginDTO.getUsername());
        return Result.success(loginVO);
    }

    /**
     * 获取钉钉登录URL
     *
     * @return 钉钉登录URL
     */
    @GetMapping("/dingtalk/login-url")
    public Result<DingTalkLoginUrlVO> getDingTalkLoginUrl() {
        logger.info("获取钉钉登录URL请求");
        
        DingTalkLoginUrlVO loginUrlVO = authService.getDingTalkLoginUrl();
        
        logger.info("钉钉登录URL生成成功");
        return Result.success(loginUrlVO);
    }

    /**
     * 钉钉扫码登录回调
     *
     * @param dingTalkLoginDTO 钉钉登录信息
     * @param request HTTP请求
     * @return 登录结果
     */
    @PostMapping("/dingtalk/callback")
    @AuditLog(operationType = "DINGTALK_LOGIN", description = "钉钉扫码登录")
    public Result<LoginVO> dingTalkCallback(@Valid @RequestBody DingTalkLoginDTO dingTalkLoginDTO, 
                                           HttpServletRequest request) {
        logger.info("钉钉登录回调请求: {}", dingTalkLoginDTO);
        
        String clientIp = getClientIp(request);
        LoginVO loginVO = authService.dingTalkLogin(dingTalkLoginDTO, clientIp);
        
        logger.info("钉钉登录成功");
        return Result.success(loginVO);
    }

    /**
     * 刷新访问token
     *
     * @param tokenRefreshDTO 刷新token信息
     * @return 新的token信息
     */
    @PostMapping("/refresh")
    @AuditLog(operationType = "REFRESH_TOKEN", description = "刷新Token")
    public Result<LoginVO> refreshToken(@Valid @RequestBody TokenRefreshDTO tokenRefreshDTO) {
        logger.info("Token刷新请求");
        
        LoginVO loginVO = authService.refreshToken(tokenRefreshDTO);
        
        logger.info("Token刷新成功");
        return Result.success(loginVO);
    }

    /**
     * 用户登出
     *
     * @param request HTTP请求
     * @return 登出结果
     */
    @PostMapping("/logout")
    @AuditLog(operationType = "LOGOUT", description = "用户登出")
    public Result<Void> logout(HttpServletRequest request) {
        logger.info("用户登出请求");
        
        String token = extractTokenFromRequest(request);
        if (token != null) {
            authService.logout(token);
        }
        
        logger.info("用户登出成功");
        return Result.success();
    }

    /**
     * 验证token有效性
     *
     * @param request HTTP请求
     * @return 验证结果
     */
    @GetMapping("/validate")
    public Result<Boolean> validateToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return Result.success(false);
        }
        
        boolean valid = authService.validateToken(token);
        return Result.success(valid);
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 从请求中提取JWT token
     *
     * @param request HTTP请求
     * @return JWT token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
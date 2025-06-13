package com.biou.project.dto;

import javax.validation.constraints.NotBlank;

/**
 * Token刷新请求DTO
 * 
 * @author Jax
 * @since 2024-01-01
 */
public class TokenRefreshDTO {

    @NotBlank(message = "刷新token不能为空")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "TokenRefreshDTO{" +
                "refreshToken='[PROTECTED]'" +
                '}';
    }
}
package com.biou.project.dto;

import javax.validation.constraints.NotBlank;

/**
 * 钉钉扫码登录请求DTO
 * 
 * @author Jax
 * @since 2024-01-01
 */
public class DingTalkLoginDTO {

    @NotBlank(message = "授权码不能为空")
    private String code;

    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DingTalkLoginDTO{" +
                "code='" + code + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
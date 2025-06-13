package com.biou.project.vo;

/**
 * 钉钉登录URL响应VO
 * 
 * @author Jax
 * @since 2025-06-14
 */
public class DingTalkLoginUrlVO {

    private String loginUrl;
    private String state;
    private Long expiresIn;

    public DingTalkLoginUrlVO() {
        this.expiresIn = 300L; // 5分钟过期
    }

    public DingTalkLoginUrlVO(String loginUrl, String state) {
        this();
        this.loginUrl = loginUrl;
        this.state = state;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
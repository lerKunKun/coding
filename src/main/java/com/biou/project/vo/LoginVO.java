package com.biou.project.vo;

/**
 * 登录响应VO
 * 
 * @author Jax
 * @since 2024-01-01
 */
public class LoginVO {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfoVO userInfo;

    public LoginVO() {
        this.tokenType = "Bearer";
    }

    public LoginVO(String accessToken, String refreshToken, Long expiresIn, UserInfoVO userInfo) {
        this();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVO userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 用户信息VO
     */
    public static class UserInfoVO {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String realName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }
}
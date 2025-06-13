package com.biou.project.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 钉钉API工具类
 * 
 * @author Jax
 * @since 2025-06-14
 */
@Component
public class DingTalkUtils {

    @Value("${dingtalk.app-id}")
    private String appId;

    @Value("${dingtalk.app-secret}")
    private String appSecret;

    @Value("${dingtalk.redirect-uri}")
    private String redirectUri;

    private static final String DINGTALK_API_BASE = "https://oapi.dingtalk.com";
    private static final String ACCESS_TOKEN_URL = DINGTALK_API_BASE + "/gettoken";
    private static final String USER_INFO_URL = DINGTALK_API_BASE + "/topapi/v2/user/get";
    private static final String USER_ACCESS_TOKEN_URL = DINGTALK_API_BASE + "/sns/getuserinfo_bycode";

    /**
     * 获取钉钉扫码登录URL
     *
     * @param state 状态参数，用于防止CSRF攻击
     * @return 登录URL
     */
    public String getLoginUrl(String state) {
        return "https://login.dingtalk.com/oauth2/auth" +
                "?response_type=code" +
                "&client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&scope=openid" +
                "&state=" + state +
                "&prompt=consent";
    }

    /**
     * 获取企业访问token
     *
     * @return access_token
     * @throws IOException 网络请求异常
     */
    public String getAccessToken() throws IOException {
        String url = ACCESS_TOKEN_URL + "?appkey=" + appId + "&appsecret=" + appSecret;
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject.getInteger("errcode") == 0) {
                return jsonObject.getString("access_token");
            } else {
                throw new RuntimeException("获取钉钉access_token失败: " + jsonObject.getString("errmsg"));
            }
        }
    }

    /**
     * 通过授权码获取用户信息
     *
     * @param code 授权码
     * @return 用户信息
     * @throws IOException 网络请求异常
     */
    public JSONObject getUserInfoByCode(String code) throws IOException {
        String accessToken = getAccessToken();
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("code", code);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(USER_ACCESS_TOKEN_URL + "?access_token=" + accessToken);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
            
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject.getInteger("errcode") == 0) {
                return jsonObject.getJSONObject("user_info");
            } else {
                throw new RuntimeException("获取钉钉用户信息失败: " + jsonObject.getString("errmsg"));
            }
        }
    }

    /**
     * 通过用户ID获取详细用户信息
     *
     * @param userId 钉钉用户ID
     * @return 用户详细信息
     * @throws IOException 网络请求异常
     */
    public JSONObject getUserDetailById(String userId) throws IOException {
        String accessToken = getAccessToken();
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("userid", userId);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(USER_INFO_URL + "?access_token=" + accessToken);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
            
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject.getInteger("errcode") == 0) {
                return jsonObject.getJSONObject("result");
            } else {
                throw new RuntimeException("获取钉钉用户详细信息失败: " + jsonObject.getString("errmsg"));
            }
        }
    }

    /**
     * 验证回调签名（如果钉钉配置了回调签名验证）
     *
     * @param signature 钉钉传递的签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 是否验证通过
     */
    public boolean verifyCallback(String signature, String timestamp, String nonce) {
        // 这里可以根据钉钉的签名验证规则实现
        // 通常使用HMAC-SHA256算法
        return true; // 暂时返回true，实际项目中需要实现具体的验证逻辑
    }
}
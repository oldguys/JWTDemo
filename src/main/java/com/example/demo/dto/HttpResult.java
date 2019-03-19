package com.example.demo.dto;

/**
 * Created by Administrator on 2019/3/16 0016.
 */
public class HttpResult {

    private String result;

    private String token;

    public HttpResult(String result, String token) {
        this.result = result;
        this.token = token;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

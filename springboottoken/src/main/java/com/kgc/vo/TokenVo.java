package com.kgc.vo;

/**
 * token视图对象结构
 */
public class TokenVo {
    private String token;//token的字符串，唯一的标识
    private Long genTime;//创建时间
    private Long expTime;//有效期时间

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getGenTime() {
        return genTime;
    }

    public void setGenTime(Long genTime) {
        this.genTime = genTime;
    }

    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }
}

package com.kgc.pojo;

public class User {
    private Integer id;
    private String name;
    private String pwd;
    private String nickname;

    public User() {
        super();
    }

    public User(Integer id, String name, String pwd, String nickname) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

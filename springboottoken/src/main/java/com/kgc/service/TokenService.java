package com.kgc.service;

import com.kgc.pojo.User;

import java.text.ParseException;

public interface TokenService {
    //生成Token
    public String generateToken(String userAgent, User user);
    //保存到reids
    public void saveRedis(String token,User user) ;
    //根据token获取对象
    public User getUserInfo(String token) ;
    //删除
    public boolean delete(String token);

    //置换
    public String reloadToken(String userAgent,String token)throws ParseException;
}

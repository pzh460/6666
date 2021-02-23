package com.kgc.service;

import com.kgc.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    public User login( String name, String pwd);
}

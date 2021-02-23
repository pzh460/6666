package com.kgc.controller;

import com.kgc.pojo.User;
import com.kgc.service.TokenService;
import com.kgc.service.UserService;
import com.kgc.util.Dto;
import com.kgc.util.DtoUtil;
import com.kgc.vo.TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @PostMapping("/login")
    public Dto login(String name, String pwd, HttpServletRequest request){

        //马儿...

        User userLogin = userService.login(name, pwd);
        if(userLogin!=null) {
            //获取用户代理字符串
            String userAgent=request.getHeader("User-Agent");
            System.out.println(userAgent);
            //生成token
            String token = tokenService.generateToken(userAgent, userLogin);
            System.out.println(token);
            //保存到redis中
            tokenService.saveRedis(token,userLogin);
            //封装一个返回给浏览器的对象vo
            TokenVo tokenVo=new TokenVo();
            tokenVo.setToken(token);
            tokenVo.setGenTime(System.currentTimeMillis());
            tokenVo.setExpTime(System.currentTimeMillis()+2*60*60*1000);//毫秒

            return DtoUtil.returnSuccess("ok", tokenVo);
        }else{
            //失败
            return DtoUtil.returnFail("登陆失败","0000");
        }
    }
    @PostMapping("/getUser")
    public Dto getUserByToken(HttpServletRequest request){
        //从请求头中获取token
        String token=request.getHeader("token");
        //调用业务
        User userInfo = tokenService.getUserInfo(token);
        if(userInfo!=null){
            return DtoUtil.returnSuccess("ok",userInfo);
        }else{
            return DtoUtil.returnFail("not","0001");
        }

    }

    @RequestMapping("/exit")
    public Dto exit(HttpServletRequest request){
        //根据请求头获取token
        String token=request.getHeader("token");
        //调用业务
        if(tokenService.delete(token)){
            return DtoUtil.returnSuccess("ok");
        }else{
            return DtoUtil.returnFail("input","0012");
        }
    }
    //置换
    @RequestMapping("/reload")
    public Dto reload(HttpServletRequest request){
        //获取用户代理
        String userAgent=request.getHeader("User-Agent");
        //请求头中获取token
        String token=request.getHeader("token");
        //调用置换业务
        try {
            String newtoken = tokenService.reloadToken(userAgent, token);
            //封装一个token vo对象
            TokenVo tokenVo=new TokenVo();
            tokenVo.setToken(newtoken);
            tokenVo.setGenTime(System.currentTimeMillis());
            tokenVo.setExpTime(System.currentTimeMillis()+2*60*60*1000);//毫秒
            return DtoUtil.returnSuccess("ok", tokenVo);
        } catch (ParseException e) {
            e.printStackTrace();
            //return DtoUtil.returnSuccess("error", e);
            return DtoUtil.returnFail("error",e,"0111");
        }
    }

}


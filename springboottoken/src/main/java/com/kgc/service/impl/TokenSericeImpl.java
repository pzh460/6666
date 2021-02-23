package com.kgc.service.impl;

import com.alibaba.fastjson.JSON;
import com.kgc.pojo.User;
import com.kgc.service.TokenService;
import com.kgc.util.MD5;
import com.kgc.util.RedisUtils;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TokenSericeImpl implements TokenService {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public String generateToken(String userAgent, User user) {
        StringBuffer sb=new StringBuffer("token:");
        //获取用户代理对象
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        //判断系统是移动端
        if(agent.getOperatingSystem().isMobileDevice()){
            sb.append("MOBILE-");
        }else{
            sb.append("PC-");
        }
        //加密
        sb.append(MD5.getMd5(user.getName(),32)+"-");
        sb.append(user.getId()+"-");
        sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"-");
        sb.append(MD5.getMd5(userAgent,6));
        return sb.toString();
    }
    @Override
    public void saveRedis(String token, User user) {
        if(token.startsWith("token:MOBILE-")){
            //永久有效期
            redisUtils.set(token, JSON.toJSONString(user));

        }else{
            //2个小时的有效期
            redisUtils.set(token,2*60*60,JSON.toJSONString(user));
        }
    }
    @Override
    public User getUserInfo(String token) {
        String jsonstr= redisUtils.get(token).toString();
        User user=JSON.parseObject(jsonstr,User.class);
        return user;
    }
    @Override
    public boolean delete(String token) {
        if(redisUtils.exist(token)){
            redisUtils.delete(token);
            return true;
        }
        return false;
    }

    long time=119*60*1000;//毫秒
    //置换
    @Override
    public String reloadToken(String userAgent, String token) throws ParseException {
        if(!redisUtils.exist(token)){
            throw  new RuntimeException("token不存在，无法置换");
        }
        //获取token的创建时间
        String strtime=token.split("-")[3];
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(strtime);
        System.out.println("date.getTime()"+date.getTime());
        //计算是否达到置换的时间
        long diff=120*60*1000-(Calendar.getInstance().getTimeInMillis()-date.getTime());
        System.out.println(diff);
        if(diff>time){
            throw  new RuntimeException("不到置换时间，无法置换，还剩"+(diff/1000)+"秒");
        }
        //根据旧的token来获取用户对象
        String jsonstr = redisUtils.get(token).toString();
        User user =  JSON.parseObject(jsonstr, User.class);
        //生成新的token
        String newToken=this.generateToken(userAgent,user);
        System.out.println("新的token"+newToken);
        //保存到redis中
        this.saveRedis(newToken,user);
        //删除旧的token
        this.delete(token);
        return newToken;
    }
}

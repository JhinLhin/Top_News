package com.atguigu.service.impl;

import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author xuyih
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-08-04 13:27:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    //Login Service
    @Override
    public Result login(User user) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(wrapper);

        //Check if username exist
        if (loginUser == null)  return Result.build(null, ResultCodeEnum.USERNAME_ERROR);

        //Check if password is valid
        if (!StringUtils.isEmpty(user.getUserPwd())
                && MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())) {
            // Successfully Logged in
            //Generate token by user id
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            //Return result with the token map, map offers more flexibility and stores key-value pair
            Map data = new HashMap();
            data.put("token", token);
            return Result.ok(data);
        }

        return Result.build(loginUser, ResultCodeEnum.PASSWORD_ERROR);
    }

    //Get UserInfo by parsing token
    @Override
    public Result getUserInfo(String token) {
        boolean expiration = jwtHelper.isExpiration(token);
        //Token expired
        if (expiration) return Result.build(null, ResultCodeEnum.NOTLOGIN);

        int userId = jwtHelper.getUserId(token).intValue();
        User user = userMapper.selectById(userId);
        //To prevent password from leaking
        user.setUserPwd("");
        Map data = new HashMap();
        data.put("loginUser", user);
        return Result.ok(data);
    }

    //Check if username is unique in the database
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(wrapper);
        if (count == 0) return Result.ok(null);
        return Result.build(count, ResultCodeEnum.USERNAME_USED);
    }

    //register an account
    @Override
    public Result register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) return Result.build(null, ResultCodeEnum.USERNAME_USED);
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return Result.ok(null);

    }

}





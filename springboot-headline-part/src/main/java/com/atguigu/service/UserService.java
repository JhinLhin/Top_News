package com.atguigu.service;

import com.atguigu.pojo.User;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xuyih
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-08-04 13:27:38
*/
public interface UserService extends IService<User> {

    //Login Service
    Result login(User user);

    //Get userInfo by token
    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result register(User user);
}

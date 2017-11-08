package com.lanou.service;

import com.lanou.bean.User;

/**
 * Created by dllo on 17/11/8.
 */
public interface UserService {

    User findUserByUserName(String name);

}

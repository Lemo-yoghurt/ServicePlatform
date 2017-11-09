package com.lanou.service;

import com.lanou.bean.User;

import java.util.List;

/**
 * Created by dllo on 17/11/8.
 */
public interface UserService {

    User findUserByUserName(String name);

    List<User> getAllAdmin();

}

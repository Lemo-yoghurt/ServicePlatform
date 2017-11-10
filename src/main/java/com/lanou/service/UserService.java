package com.lanou.service;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.User;

import java.util.List;

/**
 * Created by dllo on 17/11/8.
 */
public interface UserService {

    User findUserByUserName(String name);

    List<User> getAllAdmin();

    PageInfo<User> queryUserByPage(Integer pageNo,Integer pageSize);

    User findUserByUserId(Integer id);

    void updateUser(User user);

    void insertUser(User user);
}

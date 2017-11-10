package com.lanou.mapper;

import com.lanou.bean.Role;
import com.lanou.bean.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findUserByUserName(String name);

    List<User> findAllAdmin();

    List<User> findUsersByRoleId(Integer roleId);
}
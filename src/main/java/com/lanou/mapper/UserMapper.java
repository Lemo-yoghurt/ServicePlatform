package com.lanou.mapper;

import com.lanou.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    //根据日期范围或者管理员名称查询用户
    List<User> findUserByCondition(@Param("datemin") String datemin,
                                   @Param("datemax") String datemax,
                                   @Param("username") String username);
}
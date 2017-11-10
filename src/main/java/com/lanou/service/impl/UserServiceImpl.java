package com.lanou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanou.bean.User;
import com.lanou.mapper.UserMapper;
import com.lanou.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dllo on 17/11/8.
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User findUserByUserName(String name) {
        return userMapper.findUserByUserName(name);
    }

    @Override
    public List<User> getAllAdmin() {
        return userMapper.findAllAdmin();
    }

    //分页
    @Override
    public PageInfo<User> queryUserByPage(Integer pageNo, Integer pageSize) {

        //判断参数的合法性
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;

        PageHelper.startPage(pageNo, pageSize);

        List<User> users = userMapper.findAllAdmin();

        PageInfo<User> pageInfo = new PageInfo<>(users);


        return pageInfo;
    }

    //通过id查找
    @Override
    public User findUserByUserId(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    //修改user
    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    //添加用户
    @Override
    public void insertUser(User user) {
       userMapper.insertSelective(user);
    }


}

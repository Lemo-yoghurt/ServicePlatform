package com.lanou.service.impl;

import com.lanou.bean.Role;
import com.lanou.mapper.RoleMapper;
import com.lanou.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author dllo
 * @date 2017/11/9
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRole() {
        return roleMapper.findAllRole();
    }

    @Override
    public Integer roleDelById(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }
}

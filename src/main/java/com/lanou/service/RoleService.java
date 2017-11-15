package com.lanou.service;

import com.lanou.bean.Role;

import java.util.List;

/**
 *
 * @author dllo
 * @date 2017/11/9
 */
public interface RoleService {

    /**
     * 显示所有
     * @return List<Role>
     */
    List<Role> getAllRole();

    /**
     * 通过id删除
     * @return 删除成功/失败
     */
    Integer roleDelById(Integer id);

}

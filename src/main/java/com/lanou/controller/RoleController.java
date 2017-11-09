package com.lanou.controller;

import com.lanou.bean.Role;
import com.lanou.mapper.RoleMapper;
import com.lanou.service.RoleService;
import com.lanou.utils.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色管理
 * @author dllo
 * @date 2017/11/9
 */
@Controller
public class RoleController {

    @Resource
    private RoleService roleService;

    //获取全部的管理员
    @ResponseBody
    @RequestMapping(value = "/getAllRole")
    public AjaxResult getAllAdmin() {

        List<Role> roles = roleService.getAllRole();
        System.out.println(roles);

        return new AjaxResult(roles);

    }

    /**
     * 跳转添加页面
     * @return
     */
    @RequestMapping(value = "/roleAdd")
    public String roleAdd() {
        return "admin-role-add";
    }

}

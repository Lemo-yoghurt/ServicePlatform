package com.lanou.shiro;


import com.lanou.bean.User;
import com.lanou.mapper.UserMapper;
import com.lanou.service.UserService;
import com.lanou.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dllo on 17/11/7.
 */

public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;



    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User) principalCollection.getPrimaryPrincipal();

        //可以获取user的用户id及各种信息
        List<String> perList = Arrays.asList("user:query", "user:update");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        for (String per : perList) {
            info.addStringPermission(per);
        }

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();


        User  user = userService.findUserByUserName(username);

        System.out.println(user);

        if (user == null) {

            throw new UnknownAccountException("用户名不对");
        }
        String password = new String((char[]) authenticationToken.getCredentials());

        if (!(user.getPassword().equals(password))) {
            throw new IncorrectCredentialsException("密码不对");
        }


        return new SimpleAuthenticationInfo(user, password, getName());

    }

}

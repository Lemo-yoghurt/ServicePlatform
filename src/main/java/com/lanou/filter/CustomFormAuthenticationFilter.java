package com.lanou.filter;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by dllo on 17/11/9.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {



    @Override
    protected boolean isRememberMe(ServletRequest request) {
        return super.isRememberMe(request);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        //从session中获取正确验证码
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        //取出session中正确的验证码
        String verifycode = (String) session.getAttribute("verifyCode");



        //取出页面的验证码
        //输入的验证码和session中的验证码进行比对
        String randomcode = httpServletRequest.getParameter("randomcode");


        if (randomcode != null && verifycode != null && !randomcode.equalsIgnoreCase(verifycode)){

            //如果验证失败,将验证码错误失败信息,通过shiroLoginFailure设置到request中
            httpServletRequest.setAttribute("shiroLoginFailure","randomCodeError");
            return true;
        }

        return super.onAccessDenied(request, response);
    }
}

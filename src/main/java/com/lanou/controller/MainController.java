package com.lanou.controller;

import com.lanou.bean.User;
import com.lanou.exception.CustomException;
import com.lanou.service.UserService;
import com.lanou.utils.AjaxResult;
import com.lanou.utils.VerifyCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by dllo on 17/11/8.
 */
@Controller
public class MainController {


    @Resource
    private UserService userService;
    /**
     *
     * @return
     */
    @RequestMapping(value = "/")
    public String home(Model model) {
        //从shiro的session中取user
        Subject subject = SecurityUtils.getSubject();

        User user = (User) subject.getPrincipal();

        System.out.println(user);

        model.addAttribute("user",user);

        return "index";
    }


    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }



    @ResponseBody
    @RequestMapping(value = "/verifycode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //用于处理真正的返回结果
        VerifyCode verifyCode = new VerifyCode();//创建工具类对象

        BufferedImage image = verifyCode.getImage();//验证码工具生成图片对象


//        将验证码保存到session中
        request.getSession().setAttribute("verifyCode", verifyCode.getText());

        //获得response对象的输出流用于图像的写入
        OutputStream os = response.getOutputStream();

        VerifyCode.output(image, os);//将图片对象
    }


    @RequestMapping("/login")
    public String login() throws IOException {

        if(SecurityUtils.getSubject().isAuthenticated()){
            return "index";
        }
        return "login";
    }

    //用户登录表单验证
    @RequestMapping(value = "/loginsubmit")
    public String loginsubmit(HttpServletRequest request) throws Exception {
        String exceptionClassName =
                (String) request.getAttribute("shiroLoginFailure");
        if (exceptionClassName.equals(UnknownAccountException.class.getName())){
            throw new CustomException("账户名不存在");
        }else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)){
            throw  new CustomException("密码错误");
        }else if ("randomCodeError".equals(exceptionClassName)){
            throw new CustomException("验证码错误");
        }else {
            throw new Exception();
        }

    }

    //获取全部的管理员
    @ResponseBody
    @RequestMapping(value = "/getAllAdmin")
    public AjaxResult getAllAdmin() {

        List<User> users = userService.getAllAdmin();
        System.out.println(users);

        return new AjaxResult(users);

    }

    @RequestMapping(value = "/admin-list")
    public String role() {
        return "admin-list";
    }

    /**
     * 角色管理
     * @return /admin-role
     */
    @RequestMapping(value = "/admin-role")
    public String adminPermission() {
        return "admin-role";
    }

}

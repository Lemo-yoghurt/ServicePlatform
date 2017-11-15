package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.User;
import com.lanou.exception.CustomException;
import com.lanou.service.UserService;
import com.lanou.utils.AjaxResult;
import com.lanou.utils.VerifyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by dllo on 17/11/8.
 */
@Controller
public class MainController {

    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

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

        logger.info(user);

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
        logger.info(users);

        return new AjaxResult(users);

    }

    //分页
    @ResponseBody
    @RequestMapping(value = "/getAllUsers")
    public PageInfo<User> getAllUsers(@RequestParam("no") Integer pageNo,
                                      @RequestParam("size") Integer pageSize){

        PageInfo<User> pageInfo = userService.queryUserByPage(pageNo, pageSize);

        return pageInfo;
    }

    @RequestMapping(value = "/admin-list")
    public String role() {
        return "admin-list";
    }

    //添加页面
    @RequestMapping(value = "/admin-add")
    public String addAdmin(){
        return "admin-add";
    }

    //修改页面
    @RequestMapping(value = "/admin-modi")
    public String modiAdmin(){
        return "admin-modi";
    }

    /**
     * 角色管理
     * @return /admin-role
     */
    @RequestMapping(value = "/admin-role")
    public String adminPermission() {
        return "admin-role";
    }

    //禁用或开启
    @ResponseBody
    @RequestMapping(value = "/openOrPause")
    public AjaxResult openOrPause(@RequestParam("uid") Integer uid,
                                  @RequestParam("flag") Integer flag){
          User user = userService.findUserByUserId(uid);
          if (flag == 0){
              user.setState(0);
          }else if (flag == 1){
              user.setState(1);
          }

          userService.updateUser(user);

          return new AjaxResult(user);

    }
    //添加用户
    @ResponseBody
    @RequestMapping(value = "/addAdmin",method = RequestMethod.POST)
    public AjaxResult addUser(User user){
        logger.info(user);
        user.setState(1);
        user.setCreateTime(new Date());
        userService.insertUser(user);
        return new AjaxResult(user);
    }

    //删除用户
    @ResponseBody
    @RequestMapping(value = "/delUser")
    public AjaxResult delUser(@RequestParam("uid") Integer uid) {
        userService.delUser(uid);
        return new AjaxResult(uid);
    }

    //批量删除用户
    @ResponseBody
    @RequestMapping(value = "/datadel")
    public AjaxResult datadelUser(@RequestParam("ck") Integer[] ck){
        for (Integer check : ck) {
            userService.delUser(check);
        }
        return new AjaxResult(ck);
    }

    //修改用户
    //1.将要修改的user存进session中
    @ResponseBody
    @RequestMapping(value = "/modiInfo")
    public AjaxResult modiInfo(HttpServletRequest request,
                               @RequestParam("uid") Integer uid) {
        HttpSession session = request.getSession();
        User user = userService.findUserByUserId(uid);
        session.setAttribute("user", user);
        return new AjaxResult(user);

    }

    //2.回显所要修改的信息
    @ResponseBody
    @RequestMapping(value = "/getUpdateInfo")
    public AjaxResult getUpdateInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return new AjaxResult(user);
    }

    //3.保存修改后的信息
    @ResponseBody
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public AjaxResult updateUser(User user) {
        System.out.println(user);
        userService.updateUser(user);
        return new AjaxResult(user);
    }

    //模糊查询
    @ResponseBody
    @RequestMapping(value = "/findByCondition")
    public PageInfo<User> findByCondition(@RequestParam("no") Integer pageNo,
                                          @RequestParam("size") Integer pageSize,
                                          @RequestParam("adminname") String username,
                                          @RequestParam("datemin") String datemin,
                                          @RequestParam("datemax") String datemax){
        System.out.println(datemax);
        System.out.println(datemin);
        if (username.equals("")){
            username = null;
        }if (datemax.equals("")){
            datemax = null;
        }if (datemin.equals("")){
            datemin = null;
        }
        PageInfo<User> pageInfo = userService.findUserByCondition(pageNo,pageSize,datemin,datemax,username);

        return pageInfo;
    }

}

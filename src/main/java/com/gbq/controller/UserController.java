package com.gbq.controller;

import com.gbq.po.User;
import com.gbq.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by gbqzh on 2018/3/2.
 */
@Controller
@RequestMapping("userController")
public class UserController {
    @Autowired
    private IUserService userService;
    @RequestMapping("findUserById")
    @ResponseBody
    public User findUserById(int id) {
        return userService.findUserById(id);
    }

    @RequestMapping("findUserByUsername")
    @ResponseBody
    public List<User> findUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

    @RequestMapping("addUser")
    @ResponseBody
    public void addUser(User user) {
        userService.addUser(user);
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    @RequestMapping("deleteUserById")
    @ResponseBody
    public void deleteUserById(int id) {
        userService.deleteUserById(id);
    }



}

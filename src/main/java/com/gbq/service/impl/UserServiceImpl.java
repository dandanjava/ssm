package com.gbq.service.impl;

import com.gbq.dao.UserMapper;
import com.gbq.po.User;
import com.gbq.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gbqzh on 2018/3/2.
 */
@Service("userService")
public class UserServiceImpl implements IUserService{
@Autowired
private UserMapper userMapper;


    @Override
    public User findUserById(int id) {
        return userMapper.findUserById(id);
    }

    @Override
    public List<User> findAllUser() {
        return userMapper.findAllUser();
    }

    @Override
    public List<User> findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public void addUser(User user) {
         userMapper.addUser(user);
    }

    @Override
    public void updateUser(User user) {
         userMapper.updateUser(user);
    }

    @Override
    public void deleteUserById(int id) {
        userMapper.deleteUserById(id);
    }
}

package com.gbq.service;

import com.gbq.po.User;

import java.util.List;

/**
 * Created by gbqzh on 2018/3/2.
 */
public interface IUserService {
    public User findUserById(int id);
    List<User> findAllUser();
    public List<User> findUserByUsername(String username);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUserById(int id);
}

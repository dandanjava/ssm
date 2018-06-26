package com.gbq.dao;

import java.util.List;

import com.gbq.po.User;

 public interface UserMapper {
	
	 User findUserById(int id);
	 List<User> findAllUser();
	 List<User> findUserByUsername(String username);
	
	 void addUser(User user);
	
	 void updateUser(User user);

	 void deleteUserById(int id);
	
}

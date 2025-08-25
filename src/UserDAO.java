package com.example.userservice.dao;

import com.example.userservice.entity.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(Long id);
}

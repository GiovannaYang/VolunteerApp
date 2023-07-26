package com.example.volunteerapp.Activity.User;

import android.content.Context;

import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.Service.UserService;

public class LoginValidator {

    private UserService userService;

    public LoginValidator(Context context) {
        userService = UserService.getInstance(context);
    }

    public boolean isPidValid(String pid) {
        // 检查Pid是否存在
        return userService.getUserByPid(pid) != null;
    }

    public boolean isPasswordValid(String pid, String password) {
        // 根据Pid获取用户对象
        User user = userService.getUserByPid(pid);
        if (user != null) {
            // 检查密码是否匹配
            return user.getPassword().equals(password);
        }
        return false;
    }
}


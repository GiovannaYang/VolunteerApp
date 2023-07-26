package com.example.volunteerapp.Activity.User;

import com.example.volunteerapp.Service.UserService;

import java.util.regex.Pattern;

public class RegistrationValidator {

    public static boolean isPhoneNumberValid(String phoneNumber) {
        // 定义手机号的正则表达式
        String regex = "^[0-9]{11}$"; // 11位数字

        // 使用正则表达式进行匹配验证
        return Pattern.matches(regex, phoneNumber);
    }

    public static boolean isPhoneNumberAvailable(String phoneNumber) {
        return !UserService.isUserExistsByPid(phoneNumber);
    }


    public static boolean isPasswordValid(String password) {
        // 验证密码长度是否大于等于6位小于等于30位
        if (password.length() < 6 || password.length() > 30) {
            return false;
        }
        return true;
    }

    public static boolean isNameValid(String name) {
        // 验证姓名长度是否大于1位小于等于10位
        if (name.length() < 1 || name.length() > 10) {
            return false;
        }
        return true;
    }
}


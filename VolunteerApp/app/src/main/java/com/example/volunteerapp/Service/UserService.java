package com.example.volunteerapp.Service;
import android.content.Context;

import com.example.volunteerapp.Activity.User.RegistrationValidator;
import com.example.volunteerapp.Dao.UserDao;
import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.Entity.User;

import com.example.volunteerapp.utils.GlobalData;

import java.util.List;

public class UserService {

    private static UserService instance;
    private static UserDao userDao;

    private UserService(Context context) {
        userDao = new UserDao(context);
    }

    public static synchronized UserService getInstance(Context context) {
        if (instance == null) {
            instance = new UserService(context);
        }
        return instance;
    }

    public static void login(String pid) {
        User user = userDao.getUserByPid(pid);
        GlobalData globalData = GlobalData.getInstance();
        globalData.setLoggedIn(true);
        globalData.setUserId(user.getPid());
        globalData.setName(user.getName());
        globalData.setRole(user.getRole());
    }

    public boolean register(User user) {
        // 执行注册验证逻辑
        if (!RegistrationValidator.isPhoneNumberValid(user.getPid())) {
            // 手机号格式错误
            return false;
        }

        if (!RegistrationValidator.isPhoneNumberAvailable(user.getPid())) {
            // 手机号已注册
            return false;
        }

        if (!RegistrationValidator.isPasswordValid(user.getPassword())) {
            // 密码长度不符合要求
            return false;
        }

        if (!RegistrationValidator.isNameValid(user.getName())) {
            // 姓名长度不符合要求
            return false;
        }

        // 注册用户
        return userDao.insertUser(user);
    }


    public User getUserByPid(String pid) {
        return userDao.getUserByPid(pid);
    }

    public boolean isLoggedIn() {
        GlobalData globalData = GlobalData.getInstance();
        return globalData.isLoggedIn();
    }
    public String getUserId() {
        GlobalData globalData = GlobalData.getInstance();
        return globalData.getUserId();
    }
    public int getUserRole() {
        GlobalData globalData = GlobalData.getInstance();
        return globalData.getRole();
    }
    public String getName() {
        GlobalData globalData = GlobalData.getInstance();
        return globalData.getName();
    }
    public void logout(){
        GlobalData globalData = GlobalData.getInstance();
        globalData.logout();
    }
    public boolean deleteUser() {
        GlobalData globalData = GlobalData.getInstance();
        String pid=globalData.getUserId();
        globalData.logout();
        if (!userDao.isUserExistsByPid(pid)) {
            return false;
        }
        return userDao.deleteUserByPid(pid);
    }
    public static boolean deleteUserByPid(String pid){
        if (!userDao.isUserExistsByPid(pid)) {
            return false;
        }
        return userDao.deleteUserByPid(pid);
    }
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    public void changeUserRole(String pid) {

        User user = UserDao.getUserByPid(pid);

        if (user != null) {
            int currentRole = user.getRole();
            int newRole;

            if (currentRole == GlobalData.ROLE_ADMIN) {
                newRole = GlobalData.ROLE_USER;
            } else {
                newRole = GlobalData.ROLE_ADMIN;
            }

            user.setRole(newRole);
            userDao.updateUser(user);
        }
    }
    public static boolean isUserExistsByPid(String pid) {
        return userDao.isUserExistsByPid(pid);
    }

    public void updateUsername(String newUsername) {
        GlobalData globalData = GlobalData.getInstance();
        User currentUser = getUserByPid(globalData.getUserId());
        if (currentUser != null) {
            User user=new User(currentUser.getPid(),currentUser.getPassword(),newUsername,currentUser.getRole());
            userDao.updateUsername(user);
        }
    }

    public void updatePassword(String newPassword) {
        GlobalData globalData = GlobalData.getInstance();
        User currentUser = getUserByPid(globalData.getUserId());
        if (currentUser != null) {
            User user=new User(currentUser.getPid(),newPassword,currentUser.getName(),currentUser.getRole());
            userDao.updateUserPassword(user);
        }
    }
    public List<User> searchUsersByName(String userName) {
        return userDao.searchUsersByName(userName);
    }
}


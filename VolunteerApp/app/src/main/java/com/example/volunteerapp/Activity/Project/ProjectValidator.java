package com.example.volunteerapp.Activity.Project;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProjectValidator{
    public static boolean validateProject(Context context,String name, String startTime, String endTime, String address, String content, String numberString, String telephone) {
        // 校验输入的项目信息
        if (name.isEmpty()) {
            showToast(context,"请输入项目名称");
            return false;
        }

        if (startTime.equals("Select Start Time")) {
            showToast(context,"请选择开始时间");
            return false;
        }

        if (endTime.equals("Select End Time")) {
            showToast(context,"请选择结束时间");
            return false;
        }

        if (address.isEmpty()) {
            showToast(context,"请输入项目地点");
            return false;
        }

        if (content.isEmpty()) {
            showToast(context,"请输入项目介绍");
            return false;
        }

        int number;

        if (!TextUtils.isEmpty(numberString)) {
            number = Integer.parseInt(numberString);
        } else {
            showToast(context,"请输入招募人数");
            return false;
        }


        if (telephone.isEmpty()) {
            showToast(context,"请输入联系电话");
            return false;
        }

        if (name.length()>50) {
            showToast(context,"项目名称长度不得大于50");
            return false;
        }

        // 校验结束时间不得早于开始时间
        if (!isEndTimeAfterStartTime(startTime, endTime)) {
            showToast(context,"结束时间不得早于开始时间");
            return false;
        }

        if(number<1){
            showToast(context,"招募人数不得小于1");
            return false;
        }

        if(content.length()>250){
            showToast(context,"项目介绍不得超过250字");
            return false;
        }

        if (!isValidPhoneNumber(telephone)) {
            showToast(context,"手机号必须为11位数字");
            return false;
        }

        return true;
    }

    private static boolean isEndTimeAfterStartTime(String startTime, String endTime) {
        // 解析时间字符串并比较
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            return endDate.after(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {

        String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // 检查手机号是否只包含数字字符
        if (!phoneNumber.equals(cleanedPhoneNumber)) {
            return false;
        }
        // 检查手机号长度是否为11位
        if (phoneNumber.length() != 11) {
            return false;
        }

        // 所有验证条件都通过，手机号格式有效
        return true;
    }


    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}


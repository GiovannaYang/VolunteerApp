package com.example.volunteerapp.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.volunteerapp.MainActivity;
import com.example.volunteerapp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

import java.util.regex.Pattern;

public class AddAdminActivity extends AppCompatActivity {

    private EditText etPid;
    private Button btnRegister;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        etPid = findViewById(R.id.et_pid);
        btnRegister = findViewById(R.id.btn_register);

        userService = UserService.getInstance(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = etPid.getText().toString().trim();
                String password = "123456";
                String name = "admin";

                if (pid.isEmpty()) {
                    Toast.makeText(AddAdminActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regex = "^[0-9]{11}$"; // 11位数字
                if (Pattern.matches(regex, pid)) {
                    Toast.makeText(AddAdminActivity.this, "手机号必须为11位数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(pid, password, name, 0); // Assuming role is 0 for a regular user

                if (userService.register(user)) {
                    // Registration successful
                    Toast.makeText(AddAdminActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddAdminActivity.this, ManageActivity.class);
                    startActivity(intent);
                } else {
                    // Registration failed
                    Toast.makeText(AddAdminActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
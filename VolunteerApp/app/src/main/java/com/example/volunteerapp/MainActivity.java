package com.example.volunteerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.Activity.Project.ProjectActivity;
import com.example.volunteerapp.Activity.User.IdentifyingCode;
import com.example.volunteerapp.Activity.User.LoginValidator;
import com.example.volunteerapp.Activity.User.RegisterActivity;
import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.Service.ProjectService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;

public class MainActivity extends AppCompatActivity {

    private EditText etPid;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UserService userService;
    private ImageView identifyingCode;
    private EditText code;
    private String realCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPid = findViewById(R.id.et_pid);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.text_register);
        code =findViewById(R.id.identifyingcode_edittext);
        identifyingCode=findViewById(R.id.identifyingcode_image);

        userService = UserService.getInstance(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = etPid.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String Code=code.getText().toString().toLowerCase();

                if (pid.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入完整的账号和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginValidator loginValidator = new LoginValidator(MainActivity.this);

                if (!loginValidator.isPidValid(pid)) {
                    // Pid不存在
                    Toast.makeText(MainActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!loginValidator.isPasswordValid(pid, password)) {
                    // 密码不匹配
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Code.equals(realCode)) {
                    Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                    realCode = IdentifyingCode.getInstance().getCode().toLowerCase();
                    return;
                }

                // 登录成功
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                UserService.login(pid);
                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        identifyingCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                realCode = IdentifyingCode.getInstance().getCode().toLowerCase();
            }
        });
        identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
        realCode=IdentifyingCode.getInstance().getCode().toLowerCase();
    }
}

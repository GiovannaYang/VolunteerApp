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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

public class RegisterActivity extends AppCompatActivity {

    private EditText etPid;
    private EditText etPassword;
    private EditText etName;
    private Button btnRegister;
    private EditText etConfirmPassword;
    private ImageView identifyingCode;
    private EditText code;
    private String realCode;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPid = findViewById(R.id.et_pid);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        btnRegister = findViewById(R.id.btn_register);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        code =findViewById(R.id.identifyingcode_edittext);
        identifyingCode=findViewById(R.id.identifyingcode_image);

        userService = UserService.getInstance(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入的字段值
                String pid = etPid.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String Code=code.getText().toString().toLowerCase();

                // 验证手机号格式
                if (!RegistrationValidator.isPhoneNumberValid(pid)) {
                    Toast.makeText(RegisterActivity.this, "手机号必须为11位数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 验证手机号是否已注册
                if (!RegistrationValidator.isPhoneNumberAvailable(pid)) {
                    Toast.makeText(RegisterActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 验证密码长度和匹配
                if (!RegistrationValidator.isPasswordValid(password)) {
                    Toast.makeText(RegisterActivity.this, "密码长度应为6-30位", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 验证密码和确认密码是否一致
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "密码与确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 验证姓名长度
                if (!RegistrationValidator.isNameValid(name)) {
                    Toast.makeText(RegisterActivity.this, "姓名长度应为1-10位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Code.equals(realCode)) {
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                    realCode = IdentifyingCode.getInstance().getCode().toLowerCase();
                    return;
                }

                // 执行注册逻辑
                User user = new User(pid, password, name, 0); // Assuming role is 0 for a regular user

                if (userService.register(user)) {
                    // 注册成功
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 注册失败
                    Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                }
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

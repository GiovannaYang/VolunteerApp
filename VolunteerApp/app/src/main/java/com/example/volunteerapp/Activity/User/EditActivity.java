package com.example.volunteerapp.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.volunteerapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volunteerapp.Service.UserService;

public class EditActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnSave;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnSave = findViewById(R.id.btn_save);

        userService = UserService.getInstance(this);

        String currentUsername = userService.getName();
        etUsername.setText(currentUsername);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = etUsername.getText().toString().trim();
                String newPassword = etPassword.getText().toString().trim();
                boolean isDataValid = true;

                if (!newUsername.isEmpty()||!newUsername.equals(currentUsername)) {
                    if (newUsername.length() < 1 || newUsername.length() > 10) {
                        Toast.makeText(EditActivity.this, "用户名长度应为1-10位", Toast.LENGTH_SHORT).show();
                        isDataValid = false;
                    } else {
                        userService.updateUsername(newUsername);
                    }
                }

                if (!newPassword.isEmpty()) {
                    if (newPassword.length() < 6 || newPassword.length() > 30) {
                        Toast.makeText(EditActivity.this, "密码长度应为6-30位", Toast.LENGTH_SHORT).show();
                        isDataValid = false;
                    } else {
                        userService.updatePassword(newPassword);
                    }
                }

                if (isDataValid) {
                    finish(); // Close the activity
                    Toast.makeText(EditActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

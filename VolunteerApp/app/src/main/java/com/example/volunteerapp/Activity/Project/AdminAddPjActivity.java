package com.example.volunteerapp.Activity.Project;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.ProjectService;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Locale;

import java.text.SimpleDateFormat;

public class AdminAddPjActivity extends AppCompatActivity {

    private EditText etName,etContent,etNumber,etTelephone;
    private Button btnStartTime,btnEndTime,btnSelectAddress,btnAddProject,btnPicture;

    private Calendar startCalendar,endCalendar;

    private static final int REQUEST_MAP_ADDRESS = 1;
    private static final int REQUEST_GALLERY_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpj);

        // Initialize views
        etName = findViewById(R.id.et_name);
        btnStartTime = findViewById(R.id.btn_start_time);
        btnEndTime = findViewById(R.id.btn_end_time);
        btnSelectAddress = findViewById(R.id.btn_select_address);
        etContent = findViewById(R.id.et_content);
        btnAddProject = findViewById(R.id.btn_add_project);
        etNumber = findViewById(R.id.et_number);
        etTelephone = findViewById(R.id.et_telephone);
        btnPicture = findViewById(R.id.btn_picture);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(startCalendar, true);
            }
        });

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(endCalendar, false);
            }
        });

        btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapHelper.startMapActivity(AdminAddPjActivity.this);
            }
        });

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String startTime = formatDate(startCalendar);
                String endTime = formatDate(endCalendar);
                String address = btnSelectAddress.getText().toString();
                String content = etContent.getText().toString();
                String telephone = etTelephone.getText().toString();
                String numberString = etNumber.getText().toString();
                String picture = btnPicture.getText().toString();

                boolean isValid = ProjectValidator.validateProject(AdminAddPjActivity.this, name, startTime, endTime, address, content,numberString,telephone);

                if (isValid) {
                    Project project = new Project(0, name, startTime, endTime, address, content, Integer.parseInt(numberString),telephone,picture);
                    ProjectService.getInstance(getApplicationContext()).addProject(project);
                    Toast.makeText(AdminAddPjActivity.this, "项目添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
            }
        });
    }

    private void showDateTimePicker(final Calendar calendar, final boolean isStartTime) {
        DateTimeHelper.showDateTimePicker(AdminAddPjActivity.this, calendar, new DateTimeHelper.DateTimeListener() {
            @Override
            public void onDateTimeSet(Calendar selectedDateTime) {
                if (isStartTime) {
                    startCalendar = selectedDateTime;
                    btnStartTime.setText(formatDate(startCalendar));
                } else {
                    endCalendar = selectedDateTime;
                    btnEndTime.setText(formatDate(endCalendar));
                }
            }
        });
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAP_ADDRESS && resultCode == RESULT_OK) {
            LatLng selectedLatLng = MapHelper.getLatLngFromIntent(data);
            if (selectedLatLng != null) {
                String formattedAddress = formatLatLng(selectedLatLng.latitude, selectedLatLng.longitude);
                btnSelectAddress.setText(formattedAddress);
            }
        }

        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                // 在这里处理选择的图片
                String imagePath = ImageUtils.saveImageToLocal(AdminAddPjActivity.this, imageUri); // 保存图片到本地
                btnPicture.setText(imagePath); // 将图片的本地路径赋值给项目的图片URL属性
            }
        }
    }

    private String formatLatLng(double latitude, double longitude) {
        String latitudeString = String.format(Locale.getDefault(), "%.6f", Math.abs(latitude));
        String longitudeString = String.format(Locale.getDefault(), "%.6f", Math.abs(longitude));
        String latitudeDirection = latitude >= 0 ? "°N" : "°S";
        String longitudeDirection = longitude >= 0 ? "°E" : "°W";
        return latitudeString + latitudeDirection + ", " + longitudeString + longitudeDirection;
    }
}
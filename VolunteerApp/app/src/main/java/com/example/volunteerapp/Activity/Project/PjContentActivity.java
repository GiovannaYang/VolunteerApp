package com.example.volunteerapp.Activity.Project;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.JoinService;
import com.example.volunteerapp.Service.ProjectService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PjContentActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private TextView tvProjectName;
    private TextView tvProjectBegintime;
    private TextView tvProjectEndtime;
    private TextView tvProjectAddress;
    private TextView tvProjectContent;
    private TextView tvProjectNumber;
    private TextView tvProjectTelephone;
    private Button btnEdit,btnDelete,btnState;

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pj_content);

        // 获取传递的数据
        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("project");
        JoinService joinService = JoinService.getInstance(PjContentActivity.this);
        int joinCount = joinService.getJoinCount(project.getAid());

        // 初始化视图
        mapView = findViewById(R.id.map_view);
        tvProjectName = findViewById(R.id.tv_project_name);
        tvProjectBegintime = findViewById(R.id.tv_project_begintime);
        tvProjectEndtime = findViewById(R.id.tv_project_endtime);
        tvProjectAddress = findViewById(R.id.tv_project_address);
        tvProjectContent = findViewById(R.id.tv_project_content);
        tvProjectNumber = findViewById(R.id.tv_project_number);
        tvProjectTelephone = findViewById(R.id.tv_project_telephone);

        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnState = findViewById(R.id.btn_state);

        // 设置MapView的生命周期
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // 在视图上显示项目详情数据
        tvProjectName.setText(project.getName());
        tvProjectBegintime.setText(project.getStartTime());
        tvProjectEndtime.setText(project.getEndTime());
        tvProjectAddress.setText(project.getAddress());
        tvProjectContent.setText(project.getContent());
        tvProjectNumber.setText(joinCount+"/"+ project.getNumber());
        tvProjectTelephone.setText(project.getTelephone());

        setListener();
    }
    private void setListener() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(PjContentActivity.this);

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(PjContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    int userRole = userService.getUserRole();

                    if (userRole == GlobalData.ROLE_ADMIN) {
                        // 是管理员，跳转到管理员编辑项目页面
                        Intent intent = new Intent(PjContentActivity.this, AdminEditPjActivity.class);
                        intent.putExtra("project", project);
                        startActivity(intent);
                    } else {
                        // 不是管理员，提示权限不足
                        Toast.makeText(PjContentActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(PjContentActivity.this);

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(PjContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    int userRole = userService.getUserRole();

                    if (userRole == GlobalData.ROLE_ADMIN) {
                        // 是管理员，执行删除操作
                        showDeleteConfirmationDialog();
                    } else {
                        // 不是管理员，提示权限不足
                        Toast.makeText(PjContentActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(PjContentActivity.this);
                int recruitmentCount=project.getNumber();
                JoinService joinService = JoinService.getInstance(PjContentActivity.this);
                int joinCount = joinService.getJoinCount(project.getAid());

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(PjContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    String buttonText = btnState.getText().toString();
                    if (buttonText.equals("参加")) {
                        if (joinCount >= recruitmentCount) {
                            Toast.makeText(PjContentActivity.this, "人数已满", Toast.LENGTH_SHORT).show();
                        } else {
                            showJoinConfirmationDialog();
                        }
                    } else if (buttonText.equals("已参加")) {
                        showExitConfirmationDialog();
                    }
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 在地图上添加标记
        LatLng projectLocation = new LatLng(project.getLatitude(), project.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(projectLocation).title(project.getAddress()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(projectLocation, 10f));
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage("确定要删除该项目吗？");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProject();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    private void deleteProject() {
        // Call the deleteProject method in ProjectService to delete the project
        JoinService.deleteJoinByAid(project.getAid());
        ProjectService.getInstance(PjContentActivity.this).deleteProject(project);

        // Display a success message and return to the previous activity
        Toast.makeText(PjContentActivity.this, "项目删除成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setActivityState() {
        UserService userService = UserService.getInstance(PjContentActivity.this);
        String currentTime = getCurrentTime();
        String startTime = project.getStartTime();
        String endTime = project.getEndTime();
        int recruitmentCount=project.getNumber();
        JoinService joinService = JoinService.getInstance(PjContentActivity.this);
        int joinCount = joinService.getJoinCount(project.getAid());

        boolean isJoined = JoinService.getInstance(this).isUserJoinedActivity(userService.getUserId(), project.getAid());

        if (currentTime.compareTo(startTime) < 0) {
            // 活动未开始
            if (isJoined) {
                btnState.setText("已参加");
            } else {
                if(recruitmentCount<=joinCount){
                    btnState.setText("人数已满");
                    btnState.setEnabled(false);
                }else {
                    btnState.setText("参加");
                }
            }
        } else if (currentTime.compareTo(endTime) > 0) {
            // 活动已结束
            btnState.setText("已结束");
            // 禁用按钮
            btnState.setEnabled(false);
        } else {
            // 活动进行中
            btnState.setText("进行中");
            // 禁用按钮
            btnState.setEnabled(false);
        }
    }
    private String getCurrentTime() {
        // 设置时区为东八区
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);

        // 获取当前时间
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void showJoinConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认参与");
        builder.setMessage("确定要参与该活动吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserService userService = UserService.getInstance(PjContentActivity.this);
                String pid = userService.getUserId();
                int aid = project.getAid();

                // 创建关系Join
                JoinService.getInstance(PjContentActivity.this).addJoin(aid, pid);

                // 更新按钮文字和禁用按钮
                btnState.setText("已参加");
                Toast.makeText(PjContentActivity.this, "参与成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出活动");
        builder.setMessage("确定要退出该活动吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行退出活动的操作
                exitActivity();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void exitActivity() {
        // 调用 JoinService 中的方法来退出活动
        UserService userService = UserService.getInstance(PjContentActivity.this);
        boolean isExited = JoinService.removeJoin(project.getAid(),userService.getUserId());
        if (isExited) {
            // 退出活动成功
            Toast.makeText(PjContentActivity.this, "退出活动成功", Toast.LENGTH_SHORT).show();
            // 更新按钮文本为 "参加"
            btnState.setText("参加");
        } else {
            // 退出活动失败
            Toast.makeText(PjContentActivity.this, "退出活动失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 更新项目对象
        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("project");

        // 更新视图显示的内容
        tvProjectName.setText(project.getName());
        tvProjectBegintime.setText(project.getStartTime());
        tvProjectEndtime.setText(project.getEndTime());
        tvProjectAddress.setText(project.getAddress());
        tvProjectContent.setText(project.getContent());

        // 设置活动状态
        setActivityState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

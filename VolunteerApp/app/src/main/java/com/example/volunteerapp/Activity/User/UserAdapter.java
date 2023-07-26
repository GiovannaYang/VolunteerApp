package com.example.volunteerapp.Activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

import java.util.ArrayList;
import java.util.List;
public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> users;

    public UserAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged(); // 更新列表
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvPid = convertView.findViewById(R.id.tv_pid);
            viewHolder.tvRole = convertView.findViewById(R.id.tv_role);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = users.get(position);

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvPid.setText("Tel: " + user.getPid());
        viewHolder.tvRole.setText(getRoleText(user.getRole()));

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(context);
                String currentId = userService.getUserId();

                if (currentId.equals(user.getPid())) {
                    // 当前用户是要编辑的用户，显示 Toast 提示不可编辑自己的账户
                    Toast.makeText(context, "不可编辑自己的账户", Toast.LENGTH_SHORT).show();
                } else {
                    // 弹出确认编辑对话框
                    showEditConfirmationDialog(user);
                }
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(context);
                String currentId = userService.getUserId();

                if (user.getPid().equals(currentId)) {
                    // 当前用户是要删除的用户，显示 Toast 提示不可删除自己的账户
                    Toast.makeText(context, "不可删除自己的账户", Toast.LENGTH_SHORT).show();
                } else {
                    // 弹出确认删除对话框
                    showDeleteConfirmationDialog(user);
                }
            }
        });

        return convertView;
    }


    private static class ViewHolder {
        TextView tvName;
        TextView tvPid;
        TextView tvRole;
        Button btnDelete;
        Button btnEdit;
    }

    private String getRoleText(int role) {
        switch (role) {
            case 0:
                return "普通用户";
            case 1:
                return "管理员";
            default:
                return "未知角色";
        }
    }

    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认删除账号");
        builder.setMessage("确定要删除该用户的账号吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行删除用户操作
                UserService userService = UserService.getInstance(context);
                userService.deleteUserByPid(user.getPid());
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认编辑用户权限");
        builder.setMessage("确定要改变该用户的权限吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行编辑用户权限操作
                UserService userService = UserService.getInstance(context);
                userService.changeUserRole(user.getPid());

                // 更新用户列表
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

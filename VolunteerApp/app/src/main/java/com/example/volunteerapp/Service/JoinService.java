package com.example.volunteerapp.Service;
import android.content.Context;
import com.example.volunteerapp.Dao.JoinDao;
import com.example.volunteerapp.Entity.Join;
import com.example.volunteerapp.Entity.Project;

import java.util.List;

public class JoinService {
    private static JoinService instance;
    private static JoinDao joinDao;

    private JoinService(Context context) {
        joinDao = new JoinDao(context);
    }

    public static synchronized JoinService getInstance(Context context) {
        if (instance == null) {
            instance = new JoinService(context);
        }
        return instance;
    }

    public List<Project> getProjectList(String Pid) {
        // 调用数据服务层方法获取新闻列表数据
        return joinDao.getJoinedProjects(Pid);
    }

    public boolean addJoin(int aid, String pid) {
        Join join = new Join(aid, pid);
        return joinDao.addJoin(join);
    }

    public static boolean removeJoin(int aid, String pid) {
        return joinDao.removeJoin(aid, pid);
    }

    public static boolean deleteJoinByAid(int aid){return joinDao.deleteJoinByAid(aid);}

    public static boolean deleteJoinByPid(String pid){return joinDao.deleteJoinByPid(pid);}

    public boolean isUserJoinedActivity(String pid, int aid) {
        return joinDao.isUserJoinedActivity(pid, aid);
    }

    public int getJoinCount(int aid) {
        return joinDao.getJoinCount(aid);
    }
}


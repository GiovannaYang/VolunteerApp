package com.example.volunteerapp.Service;

import android.content.Context;

import com.example.volunteerapp.Dao.ProjectDao;
import com.example.volunteerapp.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private static ProjectService instance;
    private ProjectDao projectDao;

    private ProjectService(Context context) {
        projectDao = new ProjectDao(context);
    }

    public static synchronized ProjectService getInstance(Context context) {
        if (instance == null) {
            instance = new ProjectService(context);
        }
        return instance;
    }

    public List<Project> getProjectList() {
        // 调用数据服务层方法获取新闻列表数据
        return projectDao.getAllProjects();
    }

    public void addProject(Project project) {
        projectDao.insertProject(project);
    }
    public void updateProject(Project project) { projectDao.updateProject(project); }
    public void deleteProject(Project project) { projectDao.deleteProject(project); }
    public List<Project> searchProjectsByName(String projectName) {
        return projectDao.searchProjectsByName(projectName);
    }
}

package com.example.volunteerapp.Service;

import android.content.Context;
import com.example.volunteerapp.Dao.NewsDao;
import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.Entity.Project;

import java.util.List;

public class NewsService {
    private static NewsService instance;
    private NewsDao newsDao;

    private NewsService(Context context) {
        newsDao = new NewsDao(context);
    }

    public static synchronized NewsService getInstance(Context context) {
        if (instance == null) {
            instance = new NewsService(context);
        }
        return instance;
    }

    public List<News> getAllNews() {
        return newsDao.getAllNews();
    }

    public boolean addNews(String title, String content, String time) {
        News news = new News(0,title, time, content);
        return newsDao.addNews(news);
    }

    public void updateNews(int Nid,String title, String content, String time) {
        News news = new News(Nid,title, time, content);
        newsDao.updateNews(news);
    }

    public News getNewsById(int Nid) {
        return newsDao.getNewsById(Nid);
    }
    public void deleteNews(News news) { newsDao.deleteNews(news); }
    public List<News> searchNewsByName(String newsName) {
        return newsDao.searchNewsByName(newsName);
    }
}



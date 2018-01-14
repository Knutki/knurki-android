package com.example.username.hakatonindoorway.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class CourseListLoader extends AsyncTaskLoader<List<DayCoursesDto>> {
    private List<DayCoursesDto> courses;
    private String url;

    public CourseListLoader(@NonNull Context context, boolean mock) {
        super(context);

        url = "http://51.15.41.158:8080/api/";
        if(mock)
            url += "mock/events/currentPeriod";
        else
            url += "usos/events/currentPeriod";
    }

    @Override
    protected void onStartLoading() {
        if(takeContentChanged() || courses == null)
            forceLoad();
    }

    @Nullable
    @Override
    public List<DayCoursesDto> loadInBackground() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd kk:mm").create();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestProperty("Cookie", CookieManager.getInstance().getCookie("http://51.15.41.158:8080"));
            InputStream is = conn.getInputStream();
            Scanner sc = new Scanner(is);
            sc.useDelimiter("\\A");
            String response = sc.next();
            courses = gson.fromJson(response, new TypeToken<List<DayCoursesDto>>(){}.getType());
            return courses;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        return null;
    }
}

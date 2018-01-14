package com.example.username.hakatonindoorway.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

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

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public CourseListLoader(@NonNull Context context) {
        super(context);
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
            conn = (HttpURLConnection)new URL("http://51.15.78.247:8080/api/mock/events/currentPeriod").openConnection();
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

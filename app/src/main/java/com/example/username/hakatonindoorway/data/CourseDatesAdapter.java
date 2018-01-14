package com.example.username.hakatonindoorway.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.username.hakatonindoorway.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class CourseDatesAdapter extends PagerAdapter {
    private static DateFormat TITLE_FORMAT = new SimpleDateFormat("dd.MM", Locale.US);

    private List<Date> dates = new ArrayList<>();
    private CourseListAdapter adapter;

    public CourseDatesAdapter(CourseListAdapter adapter) {
        this.adapter = adapter;
    }

    public void refresh(List<DayCoursesDto> dates) {
        this.dates.clear();
        if(dates != null)
            for(DayCoursesDto d : dates)
                this.dates.add(d.getDate());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE_FORMAT.format(dates.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context ctx = container.getContext();

        RecyclerView rv = new RecyclerView(ctx);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        adapter.setDate(dates.get(position));
        container.addView(rv);
        return rv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

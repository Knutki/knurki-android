package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.username.hakatonindoorway.data.CourseDatesAdapter;
import com.example.username.hakatonindoorway.data.CourseListAdapter;
import com.example.username.hakatonindoorway.data.CourseListLoader;
import com.example.username.hakatonindoorway.data.DayCoursesDto;

import java.util.List;

public class PlanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<DayCoursesDto>> {

    private CourseListAdapter adapter;
    private CourseDatesAdapter datesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        adapter = new CourseListAdapter();
        ViewPager vpCourses = findViewById(R.id.vpCourses);
        datesAdapter = new CourseDatesAdapter(adapter);
        vpCourses.setAdapter(datesAdapter);
        vpCourses.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.setDate(datesAdapter.getItem(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getSupportLoaderManager().initLoader(0, new Bundle(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_plan, menu);
        return true;
    }

    @Override
    public Loader<List<DayCoursesDto>> onCreateLoader(int i, Bundle bundle) {
        return new CourseListLoader(this, getIntent().getBooleanExtra("mock", false));
    }

    @Override
    public void onLoadFinished(Loader<List<DayCoursesDto>> loader, List<DayCoursesDto> data) {
        adapter.refresh(data);
        datesAdapter.refresh(data);
        if(data != null && !data.isEmpty())
            adapter.setDate(data.get(0).getDate());
    }

    @Override
    public void onLoaderReset(Loader<List<DayCoursesDto>> loader) {
        adapter.refresh(null);
        datesAdapter.refresh(null);
    }

    public void closeTab(View planView) {
        Log.d("chuj", "chuj");
        finish();
//        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}

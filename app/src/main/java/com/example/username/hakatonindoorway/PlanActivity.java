package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.username.hakatonindoorway.data.CourseDatesAdapter;
import com.example.username.hakatonindoorway.data.CourseListAdapter;
import com.example.username.hakatonindoorway.data.CourseListLoader;
import com.example.username.hakatonindoorway.data.DayCoursesDto;

import java.util.Date;
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

        setTitle("Twoje zajęcia");
        getSupportLoaderManager().initLoader(0, new Bundle(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.close:
                finish();
                break;
        }
        return true;
    }

    @Override
    public Loader<List<DayCoursesDto>> onCreateLoader(int i, Bundle bundle) {
        return new CourseListLoader(this);
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
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}

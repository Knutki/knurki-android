package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.username.hakatonindoorway.data.CourseDto;
import com.example.username.hakatonindoorway.data.CourseListAdapter;
import com.example.username.hakatonindoorway.data.CourseListLoader;

import java.util.Collections;
import java.util.List;

public class PlanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<CourseDto>> {

    private CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        RecyclerView coursesRv = findViewById(R.id.rvCourses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        coursesRv.setLayoutManager(layoutManager);
        adapter = new CourseListAdapter();
        coursesRv.setAdapter(adapter);
        DividerItemDecoration did = new DividerItemDecoration(this, layoutManager.getOrientation());
        did.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_course_list));
        coursesRv.addItemDecoration(did);

        getSupportLoaderManager().initLoader(0, new Bundle(), this);
    }

    @Override
    public Loader<List<CourseDto>> onCreateLoader(int i, Bundle bundle) {
        return new CourseListLoader(this);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<CourseDto>> loader, List<CourseDto> data) {
        adapter.refresh(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<CourseDto>> loader) {
        adapter.refresh(null);
    }
}

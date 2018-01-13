package com.example.username.hakatonindoorway.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.username.hakatonindoorway.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    List<CourseDto> courses = new ArrayList<>();

    public void refresh(List<CourseDto> courses) {
        this.courses.clear();
        if(courses != null)
            this.courses.addAll(courses);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_course, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvCourseName;
        TextView tvRoomNumber;

        static final DateFormat DATE_FORMAT = new SimpleDateFormat("kk:mm", Locale.US);

        public ViewHolder(View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
        }

        public void bind(CourseDto course) {
            tvCourseName.setText(course.getName());
            tvTime.setText(DATE_FORMAT.format(course.getStartTime()));
            tvRoomNumber.setText(course.getRoom());
        }
    }
}

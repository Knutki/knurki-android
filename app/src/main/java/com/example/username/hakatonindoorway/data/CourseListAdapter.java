package com.example.username.hakatonindoorway.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.username.hakatonindoorway.MapActivity;
import com.example.username.hakatonindoorway.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder>{

    private Date currentDate;
    private Map<Date, List<CourseDto>> courses = new HashMap<>();

    public void setDate(Date currentDate) {
        this.currentDate = currentDate;
        notifyDataSetChanged();
    }

    public void refresh(List<DayCoursesDto> courses) {
        this.courses.clear();
        if(courses != null)
            for(DayCoursesDto dto : courses) {
                this.courses.put(dto.getDate(), new ArrayList<>(dto.getEvents()));
            }
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
        holder.bind(courses.get(currentDate).get(position));
    }

    @Override
    public int getItemCount() {
        if(courses.isEmpty() || currentDate == null)
            return 0;
        return courses.get(currentDate).size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View root;
        TextView tvTime;
        TextView tvCourseName;
        TextView tvRoomNumber;
        Context context;

        static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);

        public ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            root = itemView;
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
        }

        public void bind(final CourseDto course) {
            tvCourseName.setText(course.getName());
            tvTime.setText(DATE_FORMAT.format(course.getStartTime()));
            tvRoomNumber.setText(course.getRoom());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra(MapActivity.EXTRA_ROOM_NUMBER, course.getRoom());
                    context.startActivity(intent);
                }
            });
        }
    }
}

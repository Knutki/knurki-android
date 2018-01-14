package com.example.username.hakatonindoorway.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class DayCoursesDto {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    String date;
    List<CourseDto> events;

    public Date getDate() {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CourseDto> getEvents() {
        return events;
    }
}

package com.example.username.hakatonindoorway.data;

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
    Date date;
    List<CourseDto> events;

    public Date getDate() {
        return date;
    }

    public List<CourseDto> getEvents() {
        return events;
    }
}

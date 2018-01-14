package com.example.username.hakatonindoorway.data;

import java.util.Date;

/**
 * Created by Lukasz on 2018-01-13.
 */

public class CourseDto {
    private Date start;
    private Date end;
    private String name;
    private LocationDto location;

    public Date getStartTime() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return location.faculty;
    }

    public String getRoom() {
        return location.room;
    }

    public static class LocationDto {
        private String faculty;
        private String room;
    }
}

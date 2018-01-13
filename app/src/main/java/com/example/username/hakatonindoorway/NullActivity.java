package com.example.username.hakatonindoorway;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.username.hakatonindoorway.data.CourseDto;
import com.example.username.hakatonindoorway.data.CourseListLoader;
import com.example.username.hakatonindoorway.data.DayCoursesDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class NullActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,
        LoaderManager.LoaderCallbacks<List<DayCoursesDto>>{

    private TextToSpeech speaker;
    private View root;

    private int startDayPosition;
    private int startEventPosition;
    private int dayPosition;
    private int eventPosition;
    private List<DayCoursesDto> courses;

    boolean optionSelected;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("H mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null);

        speaker = new TextToSpeech(this, this);
        root = findViewById(R.id.root);
        optionSelected = false;
    }

    @Override
    public void onInit(int status) {
        final Handler handler = new Handler();
        speaker.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(final String s) {
                if("hello".equals(s) || "readList".equals(s) || "nextDay".equals(s)) {
                    optionSelected = false;
                    root.setOnClickListener(new View.OnClickListener() {
                        int nClick = 0;

                        @Override
                        public void onClick(View view) {
                            ++nClick;
                            if (nClick == 2) {
                                optionSelected = true;
                                if ("nextDay".equals(s)) {
                                    ++dayPosition;
                                    eventPosition = 0;
                                    speaker.speak(DATE_FORMAT.format(courses.get(dayPosition).getDate()),
                                            TextToSpeech.QUEUE_ADD, null, "_");
                                } else {
                                    speaker.speak(getString(R.string.guide), TextToSpeech.QUEUE_ADD, null, "super");
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onDone(final String s) {
                if("hello".equals(s) || "nextDay".equals(s) || "readList".equals(s)) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            root.setOnClickListener(null);
                            if(optionSelected && !"nextDay".equals(s))
                                return;
                            if("hello".equals(s) || ("nextDay".equals(s) && !optionSelected))
                                speaker.speak(getString(R.string.menu), TextToSpeech.QUEUE_ADD, null, "menu");
                            if(!"readList".equals(s) && !(optionSelected && "nextDay".equals(s)))
                                return;
                            ++eventPosition;
                            if(eventPosition == courses.get(dayPosition).getEvents().size()) {
                                if(dayPosition == courses.size())
                                    speaker.speak(getString(R.string.menu), TextToSpeech.QUEUE_ADD, null, "menu");
                                speaker.speak(getString(R.string.next_day), TextToSpeech.QUEUE_ADD, null, "nextDay");
                                return;
                            }
                            String msg = formatMessage(courses.get(dayPosition).getEvents().get(eventPosition));
                            speaker.speak(msg, TextToSpeech.QUEUE_ADD, null, "readList");
                        }
                    }, 2000);
                }

                if("menu".equals(s)) {
                    dayPosition = startDayPosition;
                    eventPosition = startEventPosition;
                    root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            speaker.speak(DATE_FORMAT.format(courses.get(dayPosition).getDate()),
                                    TextToSpeech.QUEUE_ADD, null, "_");
                            String msg = formatMessage(courses.get(dayPosition).getEvents().get(eventPosition));
                            speaker.speak(msg, TextToSpeech.QUEUE_ADD, null, "readList");
                            root.setOnLongClickListener(null);
                            return true;
                        }
                    });
                }

                if ("super".equals(s)) {
                    speaker.speak(getString(R.string.menu), TextToSpeech.QUEUE_ADD, null, "menu");
                }
            }

            @Override
            public void onError(String s) {

            }
        });
        speaker.setLanguage(Locale.forLanguageTag("pl"));
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<DayCoursesDto>> onCreateLoader(int id, Bundle args) {
        return new CourseListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<DayCoursesDto>> loader, List<DayCoursesDto> data) {
        courses = data;
        Date now = new Date();
        while(dayPosition < data.size() && data.get(dayPosition).getEvents().get(eventPosition).getStartTime().before(now)) {
            moveToNextEvent();
        }
        startDayPosition = dayPosition;
        startEventPosition = eventPosition;
        if(dayPosition < data.size()) {
            CourseDto course = courses.get(dayPosition).getEvents().get(eventPosition);
            long diff = (course.getStartTime().getTime()  - System.currentTimeMillis()) / (60*1000);
            String msg = getString(R.string.course_info1, course.getName(), diff, course.getRoom());
            speaker.speak(msg, TextToSpeech.QUEUE_ADD, null, "hello");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DayCoursesDto>> loader) {

    }

    public void moveToNextEvent() {
        ++eventPosition;
        if(eventPosition == courses.get(dayPosition).getEvents().size()) {
            ++dayPosition;
            eventPosition = 0;
        }
    }

    public String formatMessage(CourseDto course) {
        String msg = getString(R.string.course_info2, TIME_FORMAT.format(course.getStartTime()),
                course.getName(), course.getRoom());
        return msg;
    }
}

package com.example.username.hakatonindoorway;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
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

public class NullActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,
        LoaderManager.LoaderCallbacks<List<DayCoursesDto>>{

    private TextToSpeech speaker;
    private View root;

    private int startDayPosition;
    private int startEventPosition;
    private int dayPosition;
    private int eventPosition;
    private List<DayCoursesDto> courses;

    private boolean optionSelected;
    private boolean force;
    private State state;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("H mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null);

        speaker = new TextToSpeech(this, this);
        root = findViewById(R.id.root);
        optionSelected = false;
        state = State.INIT;

        final GestureDetectorCompat gd = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(velocityY > 1 && Math.abs(velocityY) > Math.abs(velocityX))
                    setState(State.MENU, true);
                if(velocityY < 1 && Math.abs(velocityY) > Math.abs(velocityX)) {
                    speaker.speak(getString(R.string.user_guide), TextToSpeech.QUEUE_ADD, null,
                            "_");
                    setState(State.MENU, false);
                }
                if(velocityX > 1 && Math.abs(velocityX) > Math.abs(velocityY))
                    moveEvents(1, true);
                if(velocityX < 1 && Math.abs(velocityX) > Math.abs(velocityY))
                    moveEvents(-1, true);
                return true;
            }
        });
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(state == State.READ_LIST || state == State.MENU)
                    return gd.onTouchEvent(motionEvent);
                return false;
            }
        });
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
                                if (state == State.NEXT_DAY) {
                                    ++dayPosition;
                                    eventPosition = 0;
                                    setState(State.READ_LIST, false);
                                } else {
                                    setState(State.GUIDE, false);
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
                            if("hello".equals(s) || ("nextDay".equals(s) && !optionSelected)) {
                                setState(State.MENU, false);
                            }
                            if(!"readList".equals(s) && !(optionSelected && "nextDay".equals(s)))
                                return;
                            moveEvents(1, false);
                        }
                    }, 2000);
                }

                if("menu".equals(s)) {
                    dayPosition = startDayPosition;
                    eventPosition = startEventPosition;
                    root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            setState(State.READ_LIST, false);
                            root.setOnLongClickListener(null);
                            return true;
                        }
                    });
                }

                if ("super".equals(s)) {
                    setState(State.MENU, false);
                }
            }

            @Override
            public void onError(String s) {

            }
        });
        speaker.setLanguage(Locale.forLanguageTag("pl"));
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void setState(State state, boolean force) {
        int queueMode = TextToSpeech.QUEUE_ADD;
        if(force)
            queueMode = TextToSpeech.QUEUE_FLUSH;

        if(state == State.HELLO) {
            dayPosition = startDayPosition;
            eventPosition = startEventPosition;
            CourseDto course = courses.get(dayPosition).getEvents().get(eventPosition);
            long diff = (course.getStartTime().getTime()  - System.currentTimeMillis()) / (60*1000);
            String msg = getString(R.string.course_info1, course.getName(), diff, course.getRoom());
            speaker.speak(msg, queueMode, null, "hello");
        } else if(state == State.MENU) {
            speaker.speak(getString(R.string.menu), queueMode, null, "menu");
        } else if(state == State.READ_LIST) {
            if(this.state != State.READ_LIST) {
                speaker.speak(DATE_FORMAT.format(courses.get(dayPosition).getDate()),
                        queueMode, null, "_");
            }
            String msg = formatMessage(courses.get(dayPosition).getEvents().get(eventPosition));
            speaker.speak(msg, queueMode, null, "readList");
        } else if(state == State.NEXT_DAY) {
            speaker.speak(getString(R.string.next_day), queueMode, null, "nextDay");
        } else if(state == State.GUIDE) {
            speaker.speak(getString(R.string.guide), queueMode, null, "super");
        }

        this.state = state;
    }

    private void moveEvents(int diff, boolean force) {
        eventPosition += diff;
        if(eventPosition == courses.get(dayPosition).getEvents().size()) {
            if(dayPosition == courses.size()) {
                setState(State.MENU, force);
            } else {
                setState(State.NEXT_DAY, force);
            }
            return;
        }
        if(eventPosition < 0) {
            --dayPosition;
            if(dayPosition <= startDayPosition) {
                dayPosition = startDayPosition;
                eventPosition = startEventPosition;
            } else
                eventPosition = 0;
        }
        setState(State.READ_LIST, force);
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
            setState(State.HELLO, false);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DayCoursesDto>> loader) {

    }

    private void moveToNextEvent() {
        ++eventPosition;
        if(eventPosition == courses.get(dayPosition).getEvents().size()) {
            ++dayPosition;
            eventPosition = 0;
        }
    }

    private String formatMessage(CourseDto course) {
        String msg = getString(R.string.course_info2, TIME_FORMAT.format(course.getStartTime()),
                course.getName(), course.getRoom());
        return msg;
    }

    private enum State {
        INIT, HELLO, MENU, READ_LIST, NEXT_DAY, GUIDE
    }
}

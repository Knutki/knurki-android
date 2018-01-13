package com.example.username.hakatonindoorway;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    Runnable startVoiceMode = new Runnable() {
        @Override
        public void run() {
            Log.d("DEBUG", "starting");
            Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            finish();
            startActivity(new Intent(LoginActivity.this, NullActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Handler handler = new Handler();
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("DEBUG", "action:"+motionEvent.getAction());
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("DEBUG", "start");
                    handler.postDelayed(startVoiceMode, 2000);
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("DEBUG", "no start");
                    handler.removeCallbacks(startVoiceMode);
                }
                return true;
            }
        });
    }


    public void singWithUsos(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }
}


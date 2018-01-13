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
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private LinearLayout llHello;
    private WebView wvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Handler handler = new Handler();
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(startVoiceMode, 2000);
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(startVoiceMode);
                }
                return true;
            }
        });
        llHello = findViewById(R.id.llHello);
        wvLogin = findViewById(R.id.wvLogin);
    }


    public void singWithUsos(View view) {
        llHello.setVisibility(View.GONE);
//        wvLogin.setVisibility(View.VISIBLE);
//        wvLogin.loadUrl("http://51.15.78.247:8080/oauth/login");
        startActivity(new Intent(this, MapActivity.class));
    }
}


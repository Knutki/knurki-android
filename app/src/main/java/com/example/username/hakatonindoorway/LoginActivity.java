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
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
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
            Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            finish();
            startActivity(new Intent(LoginActivity.this, NullActivity.class));
        }
    };

    private WebView wvLogin;
    private LinearLayout llIntro;

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

        llIntro = findViewById(R.id.llIntro);
        wvLogin = findViewById(R.id.wvLogin);
    }


    public void singWithUsos(View view) {
        llIntro.setVisibility(View.GONE);
        wvLogin.setVisibility(View.VISIBLE);
        wvLogin.loadUrl("http://51.15.41.158:8080/oauth/login");
        wvLogin.setWebViewClient(new WebViewClient() {
            private int cnt = 0;

            @Override
            public void onPageFinished(WebView view, String url) {
                if("http://51.15.41.158:8080/api/usos/user".equals(url))
                    startActivity(new Intent(LoginActivity.this, PlanActivity.class));
            }
        });
    }
}


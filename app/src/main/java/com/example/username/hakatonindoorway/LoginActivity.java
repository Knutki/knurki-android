package com.example.username.hakatonindoorway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView textToSay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Handler handler = new Handler();
//        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    handler.postDelayed(startVoiceMode, 2000);
//                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacks(startVoiceMode);
//                }
//                return true;
//            }
//        });

//        llIntro = findViewById(R.id.llIntro);
//        wvLogin = findViewById(R.id.wvLogin);
          textToSay = findViewById(R.id.textToSay);
    }

    public void sayAccesibilityText(View view) {
//        System.out.println("MADAFAKA");
        textToSay.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
//        textToSay.setText("Oho zmieniliśmy");
        textToSay.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    public void repeatCommunicate(View view) {
        textToSay.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    public void nextCommunicate(View view) {
        textToSay.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    public void singWithUsos(View view) {
        llIntro.setVisibility(View.GONE);
        wvLogin.setVisibility(View.VISIBLE);
        wvLogin.loadUrl("http://51.15.41.158:8080/oauth/login");
        wvLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if("http://51.15.41.158:8080/oauth/done".equals(url)) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(LoginActivity.this, MapActivity.class));
                        }
                    }, 1500);
                }
            }
        });
    }

    public void singWithUsosMock(View view) {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        intent.putExtra("mock", true);
        startActivity(intent);
    }
}


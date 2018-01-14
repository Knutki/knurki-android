package com.example.username.hakatonindoorway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieSyncManager;

import com.indoorway.android.common.sdk.IndoorwaySdk;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndoorwaySdk.initContext(this);
        IndoorwaySdk.configure("babd13d7-abf9-4151-a965-3bffa2e0679b");

        CookieHandler.setDefault(new CookieManager());

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

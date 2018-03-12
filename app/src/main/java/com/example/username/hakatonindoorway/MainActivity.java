package com.example.username.hakatonindoorway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.indoorway.android.common.sdk.IndoorwaySdk;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndoorwaySdk.initContext(this);
    }

    public void onStartBruna(View view){
        onStartApp("65c78b42-8d25-445b-9a22-e51d969fca09");
    }

    public void onStartMiNI(View view){
        onStartApp("babd13d7-abf9-4151-a965-3bffa2e0679b");
    }

    public void onStartApp(String token){
        IndoorwaySdk.configure(token);
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}

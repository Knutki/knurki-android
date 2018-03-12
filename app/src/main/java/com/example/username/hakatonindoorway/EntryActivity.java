package com.example.username.hakatonindoorway;

/**
 * Created by mmarczuk on 11.03.18.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

public class EntryActivity extends AppCompatActivity {
    private TextView textToSay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        textToSay = findViewById(R.id.textToSay);
    }

    public void repeatCommunicate(View view) {
        textToSay.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }
}

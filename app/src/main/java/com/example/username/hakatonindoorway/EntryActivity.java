package com.example.username.hakatonindoorway;

/**
 * Created by mmarczuk on 11.03.18.
 */


import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import com.example.username.hakatonindoorway.Information.InformationModule;

public class EntryActivity extends AppCompatActivity {
    private TextView textToSay;
    private InformationModule infoModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        infoModule = new InformationModule((TextView)findViewById(R.id.textToSay));
    }

    public void prevInfo(View view) {
        infoModule.prevInfo();
    }

    public void repeatInfo(View view) {
        infoModule.repeatInfo();
    }
}

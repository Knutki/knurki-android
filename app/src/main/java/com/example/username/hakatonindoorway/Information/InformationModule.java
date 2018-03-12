package com.example.username.hakatonindoorway.Information;

import android.icu.text.IDNA;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

/**
 * Created by mmarczuk on 12.03.18.
 */

public class InformationModule {
    private TextView infoTextView;

    public InformationModule(TextView _infoTextView) {
        infoTextView = _infoTextView;
        sayInfo("Pierwszy komunikat");
    }

    public void sayInfo(String text) {
        infoTextView.setText(text);
        infoTextView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    public void prevInfo() {
        sayInfo("Poprzedni komunikat");
    }

    public void repeatInfo() {
        sayInfo("Powtarzam komunikat");
    }
}

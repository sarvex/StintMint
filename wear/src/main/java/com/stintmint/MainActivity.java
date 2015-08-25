package com.stintmint;

import android.R.color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.stintmint.R.id;
import com.stintmint.R.layout;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        this.setContentView(layout.activity_main);
        this.setAmbientEnabled();

        this.mContainerView = (BoxInsetLayout) this.findViewById(id.container);
        this.mTextView = (TextView) this.findViewById(id.text);
        this.mClockView = (TextView) this.findViewById(id.clock);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        this.updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        this.updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        this.updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (this.isAmbient()) {
            this.mContainerView.setBackgroundColor(this.getResources().getColor(color.black));
            this.mTextView.setTextColor(this.getResources().getColor(color.white));
            this.mClockView.setVisibility(View.VISIBLE);

            this.mClockView.setText(MainActivity.AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            this.mContainerView.setBackground(null);
            this.mTextView.setTextColor(this.getResources().getColor(color.black));
            this.mClockView.setVisibility(View.GONE);
        }
    }
}

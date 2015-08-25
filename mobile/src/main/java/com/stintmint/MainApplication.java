package com.stintmint;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sarvex Jatasra on 8/25/2015.
 */
public class MainApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Parse.enableLocalDatastore(this);
    Parse.initialize(this, BuildConfig.PARSE_APP_ID, BuildConfig.PARSE_API_KEY);

    final TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
    Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
  }
}

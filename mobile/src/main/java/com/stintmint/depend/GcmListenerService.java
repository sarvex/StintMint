package com.stintmint.depend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Sarvex Jatasra on 8/25/2015.
 */
public class GcmListenerService extends Service {
  @Nullable
  @Override
  public IBinder onBind(final Intent intent) {
    return null;
  }
}

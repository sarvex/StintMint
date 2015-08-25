package com.stintmint.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;
import com.stintmint.R.layout;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_main);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    final int id = item.getItemId();

    if (id == id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}

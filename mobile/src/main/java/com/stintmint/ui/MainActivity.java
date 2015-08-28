package com.stintmint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.stintmint.R;

public class MainActivity extends AppCompatActivity
    implements ApprovedStintFragment.OnFragmentInteractionListener, AcceptedStintFragment.OnFragmentInteractionListener,
    CompletedStintFragment.OnFragmentInteractionListener, OfferredStintFragment.OnFragmentInteractionListener {

  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      navigationView.setNavigationItemSelectedListener(
          new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
              menuItem.setChecked(true);
              drawerLayout.closeDrawers();
              return true;
            }
          });
    }

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    if (viewPager != null) {
      PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
      adapter.addFragment(new OfferredStintFragment(), "Offered");
      adapter.addFragment(new AcceptedStintFragment(), "Accepted");
      adapter.addFragment(new CompletedStintFragment(), "Completed");
      adapter.addFragment(new ApprovedStintFragment(), "Approved");
      viewPager.setAdapter(adapter);
      viewPager.setCurrentItem(1);
    }

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    if (ParseUser.getCurrentUser() == null) {
      toLoginActivity();
    }


  }

  private void toLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    final int id = item.getItemId();
    boolean result = false;

    switch (id) {
      case R.id.action_logout:
        ParseUser.logOutInBackground();
        toLoginActivity();
        result = true;
        break;
      case R.id.action_settings:
        result = true;
        break;
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        result = true;
      default:
        break;
    }

    return result || super.onOptionsItemSelected(item);
  }

  @Override
  public void onFragmentInteraction(String id) {

  }
}

package com.stintmint.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stintmint.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StintActivityFragment extends Fragment {

  public StintActivityFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_stint, container, false);
  }
}

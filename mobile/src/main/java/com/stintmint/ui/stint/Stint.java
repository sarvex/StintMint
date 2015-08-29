package com.stintmint.ui.stint;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sarvex Jatasra on 8/29/2015.
 */
public class Stint implements Parcelable {
  public static final Creator<Stint> CREATOR = new Creator<Stint>() {
    @Override
    public Stint createFromParcel(final Parcel in) {
      return new Stint(in);
    }

    @Override
    public Stint[] newArray(final int size) {
      return new Stint[size];
    }
  };

  public Stint() {
  }

  public Stint(Parcel in) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {

  }
}

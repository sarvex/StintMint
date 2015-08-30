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

  private int image;
  private String name;
  private String description;
  private int left;
  private int done;

  public Stint() {
  }

  public Stint(Parcel in) {
  }

  public Stint(int image, String name, String description, int left, int done) {
    this.image = image;
    this.name = name;
    this.description = description;
    this.left = left;
    this.done = done;
  }

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getDone() {
    return done;
  }

  public void setDone(int done) {
    this.done = done;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {

  }
}

package com.stintmint.ui.stint;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Sarvex Jatasra on 8/29/2015.
 */
public class StintAdapter extends RecyclerView.Adapter<StintAdapter.ViewHolder> {


  private final List<Stint> stints;

  public StintAdapter(List<Stint> stints) {
    this.stints = stints;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(Stint stint) {

    }
  }
}

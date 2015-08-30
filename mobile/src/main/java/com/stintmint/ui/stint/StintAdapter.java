package com.stintmint.ui.stint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stintmint.R;

import java.util.List;

import butterknife.Bind;
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
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stint_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bindStint(stints.get(position));
  }

  @Override
  public int getItemCount() {
    return stints.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.stint_photo) ImageView photo;
    @Bind(R.id.stint_name) TextView name;
    @Bind(R.id.stint_description) TextView description;
    @Bind(R.id.stint_left) TextView left;
    @Bind(R.id.stint_left_label) TextView leftLabel;
    @Bind(R.id.stint_done) ImageView done;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    public void bindStint(Stint stint) {
      photo.setImageResource(stint.getImage());
      name.setText(stint.getName());
      description.setText(stint.getDescription());
      left.setText(String.valueOf(stint.getLeft()));
      leftLabel.setText(" Minutes Left");
      done.setImageResource(stint.getDone());
    }
  }
}

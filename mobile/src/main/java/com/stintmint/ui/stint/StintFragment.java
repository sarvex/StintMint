package com.stintmint.ui.stint;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stintmint.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class StintFragment extends Fragment {

  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  private StintAdapter adapter;
  private List<Stint> stints;

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public static StintFragment newInstance(String param1, String param2) {
    StintFragment fragment = new StintFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public StintFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // TODO dummy data
    stints = new ArrayList<>(10);
    stints.add(new Stint(R.drawable.ic_menu, "Stint 1", "Stint One Description", 1, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 2", "Stint Two Description", 2, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 3", "Stint Three Description", 3, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 4", "Stint Four Description", 4, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 5", "Stint Five Description", 5, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 6", "Stint Six Description", 6, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 7", "Stint Seven Description", 7, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 8", "Stint Eight Description", 8, R.drawable.ic_done));
    stints.add(new Stint(R.drawable.ic_menu, "Stint 9", "Stint Nine Description", 9, R.drawable.ic_done));

    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_stint, container, false);
    ButterKnife.bind(this, view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(new StintAdapter(stints));

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      mListener = (OnFragmentInteractionListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    public void onFragmentInteraction(String id);
  }

}

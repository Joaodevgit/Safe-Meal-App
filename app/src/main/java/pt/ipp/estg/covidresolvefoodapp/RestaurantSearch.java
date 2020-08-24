package pt.ipp.estg.covidresolvefoodapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RestaurantSearch extends Fragment {

    private onFragmentRestaurantSearchInteractionListener mListener;

    public RestaurantSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_restaurant_search, container, false);
        Toolbar myToolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(myToolbar);
        //activity.onBackPressed();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentRestaurantSearchInteractionListener) {
            this.mListener = (onFragmentRestaurantSearchInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentRestaurantSearchInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }


    public interface onFragmentRestaurantSearchInteractionListener {

    }

}
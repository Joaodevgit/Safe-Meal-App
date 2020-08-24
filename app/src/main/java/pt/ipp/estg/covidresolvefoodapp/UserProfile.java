package pt.ipp.estg.covidresolvefoodapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserProfile extends Fragment {

    private onFragmentUserProfileInteractionListener mListener;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentUserProfileInteractionListener) {
            this.mListener = (onFragmentUserProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentUserProfileInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface onFragmentUserProfileInteractionListener {

    }

}
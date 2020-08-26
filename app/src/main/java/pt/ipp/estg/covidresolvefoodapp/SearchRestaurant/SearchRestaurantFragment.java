package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import pt.ipp.estg.covidresolvefoodapp.R;

public class SearchRestaurantFragment extends Fragment {

    private RadioGroup mRadioGroupEstab;
    private RadioGroup mRadioGroupCity;
    private Button mButton;

    private OnFragmentSearchRestaurantListener mListener;

    public SearchRestaurantFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_restaurant, container, false);

        this.mRadioGroupEstab = view.findViewById(R.id.radio_group_estabelicimento);
        this.mRadioGroupCity = view.findViewById(R.id.radio_group_cidade);

        this.mButton = view.findViewById(R.id.search_restaurants);

        this.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentShowRestaurants();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSearchRestaurantListener) {
            this.mListener = (OnFragmentSearchRestaurantListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentRecipeSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentSearchRestaurantListener {
        void onFragmentShowRestaurants();
    }
}
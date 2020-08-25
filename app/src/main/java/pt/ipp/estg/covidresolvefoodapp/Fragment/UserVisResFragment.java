package pt.ipp.estg.covidresolvefoodapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipp.estg.covidresolvefoodapp.Adapter.FavResAdapter;
import pt.ipp.estg.covidresolvefoodapp.Adapter.VisResAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserVisResFragment extends Fragment {

    private RecyclerView mRecyclerViewFavRes;
    private VisResAdapter mResAdapter;
    private OnFragmentUserVisResInteractionListener mListener;

    public UserVisResFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_vis_res, container, false);

        // Create contact list
        ArrayList<Restaurant> restaurants = createRestaurantsList(5);

        // Create contacts adapter
        mResAdapter = new VisResAdapter(getContext(), restaurants);

        // Set RecyclerView adapter
        mRecyclerViewFavRes = view.findViewById(R.id.mRecyclerViewVisRes);
        mRecyclerViewFavRes.setAdapter(mResAdapter);

        // Set LayoutManager
        mRecyclerViewFavRes.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    private ArrayList<Restaurant> createRestaurantsList(int qty) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        Restaurant newRestaurant;
        for (int i = 0; i < qty; i++) {
            newRestaurant = new Restaurant("Restaurante " + (i + 1));
            restaurants.add(newRestaurant);
        }
        return restaurants;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentUserVisResInteractionListener) {
            this.mListener = (OnFragmentUserVisResInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentUserVisResInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentUserVisResInteractionListener {

    }
}
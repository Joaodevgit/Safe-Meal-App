package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pt.ipp.estg.covidresolvefoodapp.Adapter.FavResAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserFavResFragment extends Fragment {

    private RecyclerView mRecyclerViewFavRes;
    private FavResAdapter mResAdapter;
    private OnFragmentUserFavResInteractionListener mListener;

    public UserFavResFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_fav_res, container, false);

        // Create contact list
        ArrayList<Restaurant> restaurants = createRestaurantsList(5);

        // Create contacts adapter
        mResAdapter = new FavResAdapter(getContext(), restaurants);

        // Set RecyclerView adapter
        mRecyclerViewFavRes = view.findViewById(R.id.mRecyclerViewFavRes);
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
        if (context instanceof UserFavResFragment.OnFragmentUserFavResInteractionListener) {
            this.mListener = (UserFavResFragment.OnFragmentUserFavResInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentUserFavResInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentUserFavResInteractionListener {

    }
}
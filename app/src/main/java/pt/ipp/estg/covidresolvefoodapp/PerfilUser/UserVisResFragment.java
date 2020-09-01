package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Adapter.VisResAdapter;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.InfoRestaurantActivity;

import static android.app.Activity.RESULT_OK;

public class UserVisResFragment extends Fragment {

    public static final int ADD_VIS_RES_REQUEST = 1;

    private RecyclerView mRecyclerViewVisRes;
    private VisResAdapter mResAdapter;
    private RestaurantViewModel restaurantViewModel;
    private OnFragmentUserVisResInteractionListener mListener;

    public UserVisResFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantViewModel = new ViewModelProvider(getActivity()).get(RestaurantViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_vis_res, container, false);

/*
        // Create contact list
        ArrayList<Restaurant> restaurants = createRestaurantsList(5);
*/

/*        // Create contacts adapter
        mResAdapter = new VisResAdapter(getContext(), restaurants);*/

        mResAdapter = new VisResAdapter();

        // Set RecyclerView adapter
        mRecyclerViewVisRes = view.findViewById(R.id.mRecyclerViewVisRes);
        mRecyclerViewVisRes.setAdapter(mResAdapter);


        restaurantViewModel.getAllVisitedRestaurants().observe(getActivity(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                mResAdapter.setRestaurants(restaurants);
            }
        });

        // Set LayoutManager
        mRecyclerViewVisRes.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerViewVisRes.setHasFixedSize(true);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                restaurantViewModel.delete(mResAdapter.getRestaurantAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Restaurante eliminado", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerViewVisRes);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (requestCode == ADD_VIS_RES_REQUEST && resultCode == RESULT_OK) {
            String resName = data.getStringExtra(InfoRestaurantActivity.EXTRA_RES_NAME);
            String city = data.getStringExtra(InfoRestaurantActivity.EXTRA_CITY);
            String address = data.getStringExtra(InfoRestaurantActivity.EXTRA_ADDRESS);
            String resImg = data.getStringExtra(InfoRestaurantActivity.EXTRA_IMAGE);

            Restaurant restaurant = new Restaurant(resName, mAuth.getCurrentUser().getEmail(), city, address, resImg);
            restaurantViewModel.insert(restaurant);

            Toast.makeText(getContext(), "Restaurant guardado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Note não guardado", Toast.LENGTH_SHORT).show();
        }
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
package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Adapter.VisResAdapter;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.R;


public class UserVisResFragment extends Fragment {

    private RecyclerView mRecyclerViewVisRes;
    private VisResAdapter mResAdapter;
    private RestaurantViewModel restaurantViewModel;

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                restaurantViewModel.delete(mResAdapter.getRestaurantAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerViewVisRes);

        return view;
    }

}
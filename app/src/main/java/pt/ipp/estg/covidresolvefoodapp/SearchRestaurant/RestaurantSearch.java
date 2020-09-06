package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import pt.ipp.estg.covidresolvefoodapp.R;

public class RestaurantSearch extends AppCompatActivity implements SearchRestaurantFragment.OnFragmentSearchRestaurantListener {

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        if (savedInstanceState == null) {
            SearchRestaurantFragment searchRestaurantFragment = new SearchRestaurantFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_restaurant_search, searchRestaurantFragment);
            fragmentTransaction.commit();
        }

        this.myToolbar = findViewById(R.id.toolbar_restaurant_search);
        setSupportActionBar(this.myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentShowRestaurants(double lat, double lon, String cuisines, String estabelecimento) {
        RestaurantShowFragment restaurantShowFragment = new RestaurantShowFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        bundle.putString("cuisines", cuisines);
        bundle.putString("establishment", estabelecimento);
        restaurantShowFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_restaurant_search, restaurantShowFragment);
        fragmentTransaction.commit();
    }
}
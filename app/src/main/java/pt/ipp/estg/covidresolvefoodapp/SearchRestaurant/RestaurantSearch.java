package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.R;

public class RestaurantSearch extends AppCompatActivity implements SearchRestaurantFragment.OnFragmentSearchRestaurantListener {

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        this.mAuth = FirebaseAuth.getInstance();

        SearchRestaurantFragment searchRestaurantFragment = new SearchRestaurantFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_restaurant_search, searchRestaurantFragment);
        fragmentTransaction.commit();

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
    public void onFragmentShowRestaurants() {
        RestaurantShowFragment restaurantShowFragment = new RestaurantShowFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_restaurant_search, restaurantShowFragment);
        fragmentTransaction.commit();
    }
}
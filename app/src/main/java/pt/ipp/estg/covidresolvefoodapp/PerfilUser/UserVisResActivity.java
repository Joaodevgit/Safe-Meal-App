package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserVisResActivity extends AppCompatActivity implements UserVisResFragment.OnFragmentUserVisResInteractionListener {

    private Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private RestaurantViewModel restaurantViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vis_res);

        this.mAuth = FirebaseAuth.getInstance();

        restaurantViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(RestaurantViewModel.class);

        UserVisResFragment userVisResFragment = new UserVisResFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_user_vis_res_container, userVisResFragment);
        fragmentTransaction.commit();

        this.myToolbar = findViewById(R.id.toolbarUserVisRes);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_delete_vis_res, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_vis_res:
                restaurantViewModel.deleteAllVisitedRestaurants();
                Toast.makeText(this, "Histórico limpo", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
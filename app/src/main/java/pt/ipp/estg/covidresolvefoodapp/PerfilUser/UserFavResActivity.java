package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserFavResActivity extends AppCompatActivity implements UserFavResFragment.OnFragmentUserFavResInteractionListener {

    private Toolbar myToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav_res);

        this.mAuth = FirebaseAuth.getInstance();

        UserFavResFragment userFavResFragment = new UserFavResFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_user_fav_res_container, userFavResFragment);
        fragmentTransaction.commit();

        this.myToolbar = findViewById(R.id.toolbarUserFavRes);
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
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


}
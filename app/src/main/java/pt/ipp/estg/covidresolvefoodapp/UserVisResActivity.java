package pt.ipp.estg.covidresolvefoodapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.Fragment.UserVisResFragment;

public class UserVisResActivity extends AppCompatActivity implements UserVisResFragment.OnFragmentUserVisResInteractionListener {

    private Toolbar myToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vis_res);

        this.mAuth = FirebaseAuth.getInstance();

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
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


}
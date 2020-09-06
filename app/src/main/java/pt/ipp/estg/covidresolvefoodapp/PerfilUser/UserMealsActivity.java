package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserMealsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meals);

        this.mToolbar = findViewById(R.id.toolbar_user_meals);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserMealsFragment userMealsAdapter = new UserMealsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_user_meals, userMealsAdapter);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
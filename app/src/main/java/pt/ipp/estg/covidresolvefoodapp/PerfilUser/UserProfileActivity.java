package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserProfileActivity extends AppCompatActivity implements UserProfileFragment.OnFragmentUserProfileInteractionListener {

    private Toolbar myToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.mAuth = FirebaseAuth.getInstance();

        this.myToolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserProfileFragment userProfileFragment = new UserProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_user_profile_container, userProfileFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentFavoriteRestaurantClick() {
        Intent favResInt = new Intent(getApplicationContext(),UserFavResActivity.class);
        startActivity(favResInt);
    }

    @Override
    public void onFragmentVisitedRestaurantClick() {
        Intent visResInt = new Intent(getApplicationContext(),UserVisResActivity.class);
        startActivity(visResInt);
    }

    @Override
    public void onFragmentUserReviewClick() {
        Intent userRevInt = new Intent(getApplicationContext(), UserReviewActivity.class);
        startActivity(userRevInt);
    }

    @Override
    public String onFragmentUserName() {
        return "Email: " + mAuth.getCurrentUser().getEmail();
    }
}
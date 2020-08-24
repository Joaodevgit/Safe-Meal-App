package pt.ipp.estg.covidresolvefoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LogIn.OnFragmentLogInInteractionListener,
        CreateAccount.OnFragmentCreateAccountInteractionListener, PrincipalPage.onButtonMainMenuClickListener,
        UserProfile.onFragmentUserProfileInteractionListener, RestaurantSearch.onFragmentRestaurantSearchInteractionListener,
        RestaurantMap.onFragmentRestaurantMapInteractionListener
{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mAuth = FirebaseAuth.getInstance();

        LogIn logIn = new LogIn();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, logIn);
        fragmentTransaction.commit();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                UserProfile userProf = new UserProfile();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, userProf);
                fragmentTransaction.commit();

                // CÓDIGO RELATIVO À ACTIVITY DEFINICOES
                Toast.makeText(getApplicationContext(), "Clicaste em perfil!", Toast.LENGTH_SHORT).show();
            case R.id.action_logout:
                // CÓDIGO RELATIVO AO LOGOUT
                Toast.makeText(getApplicationContext(), "Clicaste em logout!", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentLogInInteraction() {
        PrincipalPage principalPage = new PrincipalPage();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, principalPage);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentCreateAccountInteractionLogIn() {
        CreateAccount createAccount = new CreateAccount();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, createAccount);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentCreateAccountInteractionMenu() {
        PrincipalPage principalPage = new PrincipalPage();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, principalPage);
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonRestaurantSearchClick() {
        RestaurantSearch restaurantSearch = new RestaurantSearch();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, restaurantSearch);
        fragmentTransaction.commit();

        Toast.makeText(getApplicationContext(), "Clicaste em Pesquisar Restaurantes!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonRestaurantMapClick() {
        RestaurantMap restaurantMap = new RestaurantMap();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, restaurantMap);
        fragmentTransaction.commit();

        Toast.makeText(getApplicationContext(), "Clicaste em Restaurantes Perto De Si!", Toast.LENGTH_SHORT).show();
    }

}
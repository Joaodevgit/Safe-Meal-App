package pt.ipp.estg.covidresolvefoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.Fragment.CreateAccount;
import pt.ipp.estg.covidresolvefoodapp.Fragment.LogIn;

public class MainActivity extends AppCompatActivity implements LogIn.OnFragmentLogInInteractionListener,
        CreateAccount.OnFragmentCreateAccountInteractionListener, PrincipalPage.onButtonMainMenuClickListener {

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mAuth = FirebaseAuth.getInstance();

        LogIn logIn = new LogIn();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, logIn);
        fragmentTransaction.commit();

        this.myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.myToolbar);
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
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);

                // CÓDIGO RELATIVO À ACTIVITY DEFINICOES
                Toast.makeText(getApplicationContext(), "Clicaste em perfil!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_logout:
                this.mAuth.signOut();

                LogIn logIn = new LogIn();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, logIn);
                fragmentTransaction.commit();
                // CÓDIGO RELATIVO AO LOGOUT
                Toast.makeText(getApplicationContext(), "Clicaste em logout!", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
        Intent intent = new Intent(this, RestaurantSearch.class);
        startActivity(intent);
    }

    @Override
    public void onButtonRestaurantMapClick() {
        Toast.makeText(getApplicationContext(), "Para ir ao mapa", Toast.LENGTH_SHORT).show();
    }

}
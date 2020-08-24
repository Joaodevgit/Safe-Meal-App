package pt.ipp.estg.covidresolvefoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LogIn.OnFragmentLogInInteractionListener, CreateAccount.OnFragmentCreateAccountInteractionListener {

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

}
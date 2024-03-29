package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pt.ipp.estg.covidresolvefoodapp.Model.UserFirestore;
import pt.ipp.estg.covidresolvefoodapp.PerfilUser.UserProfileActivity;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.RestaurantMap.RestaurantMapActivity;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.RestaurantSearch;
import pt.ipp.estg.covidresolvefoodapp.Service.LocationService;

public class MainActivity extends AppCompatActivity implements LogIn.OnFragmentLogInInteractionListener,
        CreateAccount.OnFragmentCreateAccountInteractionListener, PrincipalPage.onButtonMainMenuClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference userRef = db.collection("User");

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;
    private AppBarLayout mAppBarLayout;

    private static final int REQUEST_FINE_LOCATION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);

        this.mAuth = FirebaseAuth.getInstance();

        LogIn logIn = new LogIn();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, logIn);
        fragmentTransaction.commit();

        this.myToolbar = findViewById(R.id.toolbar);

        this.mAppBarLayout = findViewById(R.id.appBarLayout);
        this.mAppBarLayout.setVisibility(View.INVISIBLE);

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
                break;

            case R.id.action_logout:
                Intent i = new Intent(this, LocationService.class);
                stopService(i);

                this.mAuth.signOut();
                this.mAppBarLayout.setVisibility(View.INVISIBLE);


                LogIn logIn = new LogIn();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, logIn);
                fragmentTransaction.commit();


                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onFragmentLogInInteraction() {
        this.mAppBarLayout.setVisibility(View.VISIBLE);

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
        this.mAppBarLayout.setVisibility(View.VISIBLE);

        String anonymous = "Anonymous" + this.mAuth.getCurrentUser().getUid().substring(0, 4);

        UserFirestore userFirestore = new UserFirestore(this.mAuth.getCurrentUser().getUid(), anonymous, this.mAuth.getCurrentUser().getEmail());

        userRef.add(userFirestore).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

        PrincipalPage principalPage = new PrincipalPage();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, principalPage);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentCancelAccountRegistryInteractionMenu() {
        LogIn logIn = new LogIn();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, logIn);
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonRestaurantSearchClick() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (wifiManager.isWifiEnabled()) {
            if (gps_enabled) {
                startActivity(new Intent(this, RestaurantSearch.class));
            } else {
                verifyGPSPermission("searching for restaurants");
            }
        } else {
            verifyWifiPermission();
        }
    }

    @Override
    public void onButtonRestaurantMapClick() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps_enabled) {
            if (wifiManager.isWifiEnabled()) {
                startActivity(new Intent(this, RestaurantMapActivity.class));
            } else {
                verifyWifiPermission();
            }
        } else {
            verifyGPSPermission("to map");
        }
    }

    private void verifyGPSPermission(String purpose) {
        new AlertDialog.Builder(this)
                .setTitle("Necessary GPS permission")
                .setMessage("This permission is necessary to access " + purpose)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Ir às definições
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void verifyWifiPermission() {
        new AlertDialog.Builder(this)
                .setTitle("Necessary Wifi permission")
                .setMessage("This permission is necessary to make search")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Ir às definições
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}

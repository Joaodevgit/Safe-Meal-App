package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.PerfilUser.UserProfileActivity;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.RestaurantMap.RestaurantMapActivity;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.RestaurantSearch;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements LogIn.OnFragmentLogInInteractionListener,
        CreateAccount.OnFragmentCreateAccountInteractionListener, PrincipalPage.onButtonMainMenuClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference userRef = db.collection("User");

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mAuth = FirebaseAuth.getInstance();

        this.userRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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

                this.mAppBarLayout.setVisibility(View.INVISIBLE);

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
        verifyWifiPermission(new Intent(this, RestaurantSearch.class));
    }

    @Override
    public void onButtonRestaurantMapClick() {
        verifyGPSPermission(new Intent(this, RestaurantMapActivity.class));
    }

    private void verifyGPSPermission(Intent intent) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão GPS necessária")
                    .setMessage("Esta permissão é necessária para poder aceder ao mapa")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Ir às definições
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            //request.cancel();
                        }
                    })
                    .show();
        } else {
            startActivity(intent);
        }
    }

    private void verifyWifiPermission(Intent intent) {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão Wifi necessária")
                    .setMessage("Esta permissão é necessária para poder fazer a pesquisa")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Ir às definições
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 44);
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            startActivity(intent);
        }
    }

}

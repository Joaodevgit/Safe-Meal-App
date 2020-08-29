package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class PrincipalPage extends Fragment implements EasyPermissions.PermissionCallbacks {

    private Button btnRestaurantSearch;
    private Button btnRestaurantMap;
    private onButtonMainMenuClickListener mListener;
    private static final int REQUEST_FINE_LOCATION = 100;

    public PrincipalPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_page, container, false);

        btnRestaurantSearch = view.findViewById(R.id.buttonRestaurantSearch);
        btnRestaurantMap = view.findViewById(R.id.buttonRestaurantMap);

        openApp();

        btnRestaurantSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonRestaurantSearchClick();
            }
        });

        btnRestaurantMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonRestaurantMapClick();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PrincipalPage.onButtonMainMenuClickListener) {
            this.mListener = (PrincipalPage.onButtonMainMenuClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onButtonMainMenuClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface onButtonMainMenuClickListener {
        void onButtonRestaurantSearchClick();

        void onButtonRestaurantMapClick();
    }

    @AfterPermissionGranted(REQUEST_FINE_LOCATION)
    private void openApp() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            //O utilizador já deu permissão anteriormente
        } else {
            // The rationale is showed when the user already denied
            // this permission and then tries to access this feature again
          /*  EasyPermissions.requestPermissions(this, "A permissão GPS é necessária para poder fazer a pesquisa"
                    , REQUEST_FINE_LOCATION, perms);*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(getContext(), "Operação Cancelada", Toast.LENGTH_SHORT).show();
       /* if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }*/
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }*/
}
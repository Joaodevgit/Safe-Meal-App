package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Service.LocationService;


public class PrincipalPage extends Fragment {

    private Button btnRestaurantSearch;
    private Button btnRestaurantMap;
    private ImageView mImageLogo;
    private onButtonMainMenuClickListener mListener;
    private LocationManager locationManager;

    private static final int REQUEST_FINE_LOCATION = 100;

    public PrincipalPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            Intent serviceIntent = new Intent(getContext(), LocationService.class);
            getContext().startService(serviceIntent);
        } else {
            verifyGPSPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_page, container, false);

        btnRestaurantSearch = view.findViewById(R.id.buttonRestaurantSearch);
        btnRestaurantMap = view.findViewById(R.id.buttonRestaurantMap);
        this.mImageLogo = view.findViewById(R.id.image_view_logo_main);
        this.mImageLogo.setImageResource(R.drawable.safe_meal_logo);

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

    private void verifyGPSPermission() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permissão GPS necessária")
                .setMessage("Esta permissão é necessária para poder avisar dos seus restaurantes favoritos")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Ir às definições
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();

    }

}
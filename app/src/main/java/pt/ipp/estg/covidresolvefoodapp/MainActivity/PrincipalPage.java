package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Service.LocationService;


public class PrincipalPage extends Fragment {

    private Button btnRestaurantSearch;
    private Button btnRestaurantMap;
    private onButtonMainMenuClickListener mListener;

    public PrincipalPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isMyServiceRunning(LocationService.class)) {
            Intent serviceIntent = new Intent(getContext(), LocationService.class);
            getContext().startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_page, container, false);

        btnRestaurantSearch = view.findViewById(R.id.buttonRestaurantSearch);
        btnRestaurantMap = view.findViewById(R.id.buttonRestaurantMap);

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

}
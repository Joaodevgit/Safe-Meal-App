package pt.ipp.estg.covidresolvefoodapp.RestaurantMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.MainActivity.MainActivity;
import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantMapActivity extends FragmentActivity {

    private static final int REQUEST_FINE_LOCATION = 100;
    private static final String DEFAULT_RESTAURANT_IMG = "https://i.postimg.cc/zfX7My2F/tt.jpg";// -> USAR QUANDO UM RESTAURANTE NÃO TIVER THUMB
    private static final String USER_MARK_IMG = "https://i.postimg.cc/FHf3J06q/you-Are-Here-Img.jpg";// -> USAR PARA MARCAR A POSIÇÃO DO UTILIZADOR

    //private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    private SupportMapFragment supportMapFragment;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        getLastLocation();

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        //Initialize fused location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(3000);
        //mLocationRequest.setFastestInterval(5000);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.restaurant_map);
        //supportMapFragment.getMapAsync(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                final List<Restaurant> restaurants = new ArrayList<>();
                if (locationResult != null) {
                    if (location == null || location.getLongitude() != locationResult.getLastLocation().getLongitude()
                            || location.getLatitude() != locationResult.getLastLocation().getLatitude()) {
                        location = locationResult.getLastLocation();
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                //mMap = googleMap;
                                restaurants.add(new Restaurant(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), "Estou aqui", USER_MARK_IMG));
                                //restaurants.add(new Restaurant("41.1480179493", "-8.6077113077", "Wow1", "https://b.zmtcdn.com/data/reviews_photos/18a/75890bf38f78f8f917ed435959ec618a_1449006689.jpg?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A"));
                                //restaurants.add(new Restaurant("41.1875441294", "-8.6989925802", "Wow2", DEFAULT_RESTAURANT_IMG));
                                addCustomMarker(restaurants, googleMap);
                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        boolean found = false;
                                        int i = 0;
                                        while (!found) {
                                            if (marker.getPosition().latitude == Double.parseDouble(restaurants.get(i).getLat()) &&
                                                    marker.getPosition().longitude == Double.parseDouble(restaurants.get(i).getLon())) {
                                                found = true;
                                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                Toast.makeText(getApplicationContext(), "Au tocaste em:" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                                            }
                                            i++;
                                        }
                                        return false;
                                    }
                                });

                            }

                        });
                    }
                }
            }

        };
        startLocationUpdates();
    }





/*    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }*/

/*    private void getCurrentLocation(final List<Restaurant> restaurants) {

        //Initialize task location
        Task<Location> task = mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if (location != null) {
                            //Sync map
                            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    //mMap = googleMap;
                                    //MapsInitializer.initialize(getApplicationContext());
                                    restaurants.add(new Restaurant(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), "Estou aqui", USER_MARK_IMG));
                                    addCustomMarker(restaurants, googleMap);
                                }
                            });
                        }
                    }
                });
    }*/


    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When permission granted
                //Call method
                List<Restaurant> restaurants = new ArrayList<>();
                //restaurants.add(new Restaurant("41.1480179493", "-8.6077113077", "Wow1", "https://b.zmtcdn.com/data/reviews_photos/18a/75890bf38f78f8f917ed435959ec618a_1449006689.jpg?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A"));
                //restaurants.add(new Restaurant("41.1875441294", "-8.6989925802", "Wow2", DEFAULT_RESTAURANT_IMG));
                restaurants.add(new Restaurant(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), "Estou aqui", USER_MARK_IMG));
                getCurrentLocation(restaurants);
            }
        }
    }*/

    private void addCustomMarker(final List<Restaurant> restaurants, final GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }

        // adding a marker with image from URL using glide image loading library
        for (int i = 0; i < restaurants.size(); i++) {
            final int temp = i;
            Glide.with(getApplicationContext()).
                    load(restaurants.get(i).getImageURL())
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            LatLng pos = new LatLng(Double.parseDouble(restaurants.get(temp).getLat()), Double.parseDouble(restaurants.get(temp).getLon()));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(restaurants.get(temp).getTitle())
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));
                        }
                    });
        }
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Retrofit getRetrofitZomatoAPI() {
        return new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ZomatoAPI getAPIZomato() {
        return this.getRetrofitZomatoAPI().create(ZomatoAPI.class);
    }

}
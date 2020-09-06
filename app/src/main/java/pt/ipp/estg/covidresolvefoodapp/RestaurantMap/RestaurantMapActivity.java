package pt.ipp.estg.covidresolvefoodapp.RestaurantMap;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.NearbyRestaurant;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.InfoRestaurantActivity;
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.restaurant_map);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    if (location == null || location.getLongitude() != locationResult.getLastLocation().getLongitude()
                            || location.getLatitude() != locationResult.getLastLocation().getLatitude()) {
                        location = locationResult.getLastLocation();
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                final List<RestaurantRetro> restaurants;

                                restaurants = callRestaurants(location, googleMap);
                                System.out.println("Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        boolean found = false;
                                        int i = 0;
                                        while (!found) {
                                            if (marker.getPosition().latitude == Double.parseDouble(restaurants.get(i).getRestaurant().getLocation().getLatitude()) &&
                                                    marker.getPosition().longitude == Double.parseDouble(restaurants.get(i).getRestaurant().getLocation().getLongitude())) {
                                                found = true;
                                                if (restaurants.get(i).getRestaurant().getId() != null) {
                                                    Intent resIntent = new Intent(getApplicationContext(), InfoRestaurantActivity.class);
                                                    resIntent.putExtra("idRestaurant", restaurants.get(i).getRestaurant().getId());
                                                    startActivity(resIntent);
                                                }
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


    private List<RestaurantRetro> callRestaurants(final Location userLocation, final GoogleMap googleMap) {
        final List<RestaurantRetro> nearbyRestaurants = new ArrayList<>();
        this.getAPIZomato().geocodeRestaurants(userLocation.getLatitude(), userLocation.getLongitude())
                .enqueue(new Callback<NearbyRestaurant>() {
                    @Override
                    public void onResponse(Call<NearbyRestaurant> call, Response<NearbyRestaurant> response) {
                        for (int i = 0; i < response.body().getNearby_restaurants().size(); i++) {
                            if (distance(userLocation.getLatitude(), userLocation.getLongitude()
                                    , Double.parseDouble(response.body().getNearby_restaurants().get(i).getRestaurant().getLocation().getLatitude()),
                                    Double.parseDouble(response.body().getNearby_restaurants().get(i).getRestaurant().getLocation().getLongitude())) <= 3000) {
                                nearbyRestaurants.add(response.body().getNearby_restaurants().get(i));
                            }
                        }
                        nearbyRestaurants.add(new RestaurantRetro(new RestaurantInfoRetro("Estou aqui", USER_MARK_IMG
                                , new pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.Location(String.valueOf(userLocation.getLatitude()),
                                String.valueOf(userLocation.getLongitude())))));
                        addCustomMarker(nearbyRestaurants, googleMap);
                    }

                    @Override
                    public void onFailure(Call<NearbyRestaurant> call, Throwable t) {
                        System.out.println(call.request());
                    }
                });

        return nearbyRestaurants;
    }


/*    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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

    private void addCustomMarker(final List<RestaurantRetro> restaurants, final GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }

        // adding a marker with image from URL using glide image loading library
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getRestaurant().getThumb().equals("")) {
                restaurants.get(i).getRestaurant().setThumb(DEFAULT_RESTAURANT_IMG);
            } else {
                final int temp = i;
                Glide.with(getApplicationContext()).
                        load(restaurants.get(i).getRestaurant().getThumb())
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                LatLng pos = new LatLng(Double.parseDouble(restaurants.get(temp).getRestaurant().getLocation().getLatitude())
                                        , Double.parseDouble(restaurants.get(temp).getRestaurant().getLocation().getLongitude()));

                                googleMap.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(restaurants.get(temp).getRestaurant().getName())
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                            }
                        });
            }
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

    public float distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

}
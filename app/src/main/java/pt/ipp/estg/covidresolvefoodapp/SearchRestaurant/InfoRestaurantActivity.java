package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.Intent;

import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.provider.CalendarContract;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewAdapter;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogBooking;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogReview;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.MainActivity.MainActivity;
import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.PerfilUser.UserReviewFragment;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.Location;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoRestaurantActivity extends AppCompatActivity implements UserReviewFragment.OnFragmentUserReviewInteractionListener,
        AlertDialogReview.DialogListener, AlertDialogBooking.DialogBookingListener {

    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference reviewRef = db.collection("Review");

    private int idRestaurant;

    private Toolbar myToolbar;

    private UserReviewAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private TextView mTextName;
    private TextView mTextCity;
    private TextView mTextEstab;
    private TextView mTextNota;
    private TextView mTextAdress;
    private TextView mTextHorarios;
    private TextView mTextIsTableBoooking;

    private ImageView mImageViewRestaurant;
    private ImageView mImageViewIsBooking;

    private RatingBar mRatingBar;

    private Button mButtonReview;
    private Button mButtonBooking;

    private RestaurantViewModel restaurantViewModel;


    private LocationManager lm;

    private android.location.Location userLocation;
    private Location restaurantLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private RestaurantInfoRetro restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        this.mAuth = FirebaseAuth.getInstance();

        restaurantViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(RestaurantViewModel.class);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(5000);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    if (userLocation == null || userLocation.getLongitude() != locationResult.getLastLocation().getLongitude()
                            || userLocation.getLatitude() != locationResult.getLastLocation().getLatitude()) {
                        userLocation = locationResult.getLastLocation();
                    }
                }
            }

        };


        this.idRestaurant = Integer.parseInt(getIntent().getStringExtra("idRestaurant"));

        this.myToolbar = findViewById(R.id.toolbar_restaurant_show);
        setSupportActionBar(this.myToolbar);

        this.mTextName = findViewById(R.id.name_restaurant_info);
        this.mTextCity = findViewById(R.id.restaurant_city_info);
        this.mTextNota = findViewById(R.id.nota_info);
        this.mTextAdress = findViewById(R.id.adress_info);
        this.mTextHorarios = findViewById(R.id.horarios_info);
        this.mTextIsTableBoooking = findViewById(R.id.is_table_booking);
        this.mTextEstab = findViewById(R.id.restaurant_establishment_info);

        this.mRatingBar = findViewById(R.id.estrelas_info);

        this.mImageViewIsBooking = findViewById(R.id.yes_no_booking);
        this.mImageViewRestaurant = findViewById(R.id.restaurant_image_info);

        this.mButtonReview = findViewById(R.id.button_review_restaurant);
        this.mButtonBooking = findViewById(R.id.button_booking_restaurant);



        this.mButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogReview alertDialogReview = new AlertDialogReview();

                alertDialogReview.show(getSupportFragmentManager(), "dialogReview");
            }
        });

        this.mButtonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogBooking alertDialogBooking = new AlertDialogBooking();

                alertDialogBooking.show(getSupportFragmentManager(), "dialogBooking");
            }
        });


        Query query = this.reviewRef.whereEqualTo("idRestaurant", idRestaurant).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ReviewFirestore> options = new FirestoreRecyclerOptions.Builder<ReviewFirestore>()
                .setQuery(query, ReviewFirestore.class)
                .build();

        this.mAdapter = new UserReviewAdapter(options);

        this.mRecyclerView = findViewById(R.id.mRecyclerview_show_reviews);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mRecyclerView.setAdapter(this.mAdapter);

        this.getAPIZomato().getRestaurant(idRestaurant).enqueue(new Callback<RestaurantInfoRetro>() {
            @Override
            public void onResponse(Call<RestaurantInfoRetro> call, Response<RestaurantInfoRetro> response) {
                restaurant = response.body();

                Restaurant newRestaurant = new Restaurant(restaurant.getName(), mAuth.getCurrentUser().getEmail(), restaurant.getLocation().getCity()
                        , restaurant.getLocation().getAddress(), restaurant.getThumb());
                restaurantViewModel.insert(newRestaurant);

                restaurantLocation = restaurant.getLocation();

                mTextName.setText("Nome: " + restaurant.getName());

                if (!restaurant.getThumb().equals("")) {
                    Picasso.get().load(restaurant.getThumb()).into(mImageViewRestaurant);
                } else {
                    Picasso.get().load("https://i.postimg.cc/zfX7My2F/tt.jpg").into(mImageViewRestaurant);
                }

                mTextCity.setText("Cidade: " + restaurant.getLocation().getCity());

                Iterator<String> itr = response.body().getEstablishment().iterator();
                String contentEstab = "";

                while (itr.hasNext()) {
                    contentEstab += itr.next();
                }

                mTextEstab.setText("Estabelecimento: " + contentEstab);

                mRatingBar.setRating(Float.parseFloat(restaurant.getUser_rating().getAggregate_rating()));
                mTextNota.setText("(" + restaurant.getUser_rating().getAggregate_rating() + ")");

                mTextAdress.setText("Endereço: " + restaurant.getLocation().getAddress());

                mTextHorarios.setText("Horarios: " + restaurant.getTimings());

                if (restaurant.getHas_table_booking() == 1) {
                    mImageViewIsBooking.setImageResource(R.drawable.tick);
                } else {
                    mImageViewIsBooking.setImageResource(R.drawable.close);
                    mButtonBooking.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<RestaurantInfoRetro> call, Throwable t) {
                System.out.println(call.request());
            }
        });

        this.mButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    verifyGPSPermission("review");
                } else {
                    System.out.println("Lat" + restaurantLocation.getLatitude() + "Lon" + restaurantLocation.getLongitude());
                    if (distance(userLocation.getLatitude(), userLocation.getLongitude(), Double.parseDouble(restaurantLocation.getLatitude()),
                            Double.parseDouble(restaurantLocation.getLongitude())) <= 3000) {
                        AlertDialogReview alertDialogReview = new AlertDialogReview();
                        alertDialogReview.show(getSupportFragmentManager(), "dialog");
                    } else {
                        Toast.makeText(getApplicationContext(), "Muito longe!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startLocationUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mAdapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addReview(float maxNotation, String contentMessage) {
        Timestamp timestamp = Timestamp.now();

        ReviewFirestore reviewFirestore = new ReviewFirestore(maxNotation, this.mAuth.getCurrentUser().getUid(), this.idRestaurant, contentMessage, timestamp);

        this.reviewRef.add(reviewFirestore);
    }


    private void verifyGPSPermission(String purpose) {

        new AlertDialog.Builder(this)
                .setTitle("Permissão GPS necessária")
                .setMessage("Esta permissão é necessária para poder fazer " + purpose)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Ir às definições
                        ActivityCompat.requestPermissions(InfoRestaurantActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).show();
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

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }


    @Override
    public void bookingReview(long date, long timeStart, long timeEnd, String description) {
        Intent intent = new Intent(Intent.ACTION_INSERT);

        System.out.println("DATE: " + date);

        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Booking: " + this.restaurant.getName());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, this.restaurant.getLocation().getAddress());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, this.restaurant.getLocation().getAddress());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description + " No restaurante " + this.restaurant.getName());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, (date + timeStart));
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (date + timeEnd));

        startActivity(intent);
    }

}
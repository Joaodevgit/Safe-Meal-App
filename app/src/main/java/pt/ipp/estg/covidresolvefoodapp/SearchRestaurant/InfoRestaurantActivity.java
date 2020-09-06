package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.provider.CalendarContract;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewAdapter;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogBooking;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogRefeicao;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogReview;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.MealsFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.Location;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoRestaurantActivity extends AppCompatActivity implements AlertDialogReview.DialogListener,
        AlertDialogBooking.DialogBookingListener, AlertDialogRefeicao.DialogMealsListener {

    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference reviewRef = db.collection("Review");
    private CollectionReference favRef = db.collection("Favorite");
    private CollectionReference mealsRef = db.collection("Meals");

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
    private TextView mDistanceRes;
    private TextView mReviewJust;

    private ImageView mImageViewRestaurant;
    private ImageView mImageViewIsBooking;
    private ImageView mImageViewIsReview;

    private RatingBar mRatingBar;

    private Button mButtonReview;
    private Button mButtonBooking;
    private Button mButtonRefeicao;

    private RestaurantViewModel restaurantViewModel;


    private LocationManager lm;

    private android.location.Location userLocation;
    private Location restaurantLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private RestaurantInfoRetro restaurant;

    private boolean favOff = false;

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
        //mLocationRequest.setInterval(3000);
        //mLocationRequest.setFastestInterval(5000);

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

        this.onNewIntent(getIntent());

//        this.idRestaurant = Integer.parseInt(getIntent().getStringExtra("idRestaurant"));

        this.myToolbar = findViewById(R.id.toolbar_restaurant_show);
        setSupportActionBar(this.myToolbar);

        this.mTextName = findViewById(R.id.name_restaurant_info);
        this.mTextCity = findViewById(R.id.restaurant_city_info);
        this.mTextNota = findViewById(R.id.nota_info);
        this.mTextAdress = findViewById(R.id.adress_info);
        this.mTextHorarios = findViewById(R.id.horarios_info);
        this.mTextIsTableBoooking = findViewById(R.id.is_table_booking);
        this.mTextEstab = findViewById(R.id.restaurant_establishment_info);
        this.mDistanceRes = findViewById(R.id.distance);
        this.mReviewJust = findViewById(R.id.review_just);

        this.mRatingBar = findViewById(R.id.estrelas_info);

        this.mImageViewIsBooking = findViewById(R.id.yes_no_booking);
        this.mImageViewIsReview = findViewById(R.id.yes_no_review);
        this.mImageViewRestaurant = findViewById(R.id.restaurant_image_info);


        this.mButtonReview = findViewById(R.id.button_review_restaurant);
        this.mButtonBooking = findViewById(R.id.button_booking_restaurant);
        this.mButtonRefeicao = findViewById(R.id.button_refeicao);

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

                // distância em km
                double atualDistance = distance(userLocation.getLatitude(), userLocation.getLongitude(), Double.parseDouble(restaurant.getLocation().getLatitude()),
                        Double.parseDouble(restaurant.getLocation().getLongitude())) * 0.001;

                if (atualDistance <= 1) {
                    mDistanceRes.setText("Distância: " + Math.round(atualDistance * 100.0) / 100.0 + " km");
                    mReviewJust.setText("(Distância <= 1 km)");
                    mReviewJust.setTextColor(Color.GREEN);
                    mImageViewIsReview.setImageResource(R.drawable.tick);
                } else {
                    mDistanceRes.setText("Distância: " + Math.round(atualDistance * 100.0) / 100.0 + " km");
                    mReviewJust.setText("(Distância > 1 km)");
                    mReviewJust.setTextColor(Color.RED);
                    mImageViewIsReview.setImageResource(R.drawable.close);
                    mButtonReview.setEnabled(false);
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
                    if (distance(userLocation.getLatitude(), userLocation.getLongitude(), Double.parseDouble(restaurantLocation.getLatitude()),
                            Double.parseDouble(restaurantLocation.getLongitude())) * 0.001 <= 1) {
                        AlertDialogReview alertDialogReview = new AlertDialogReview();
                        alertDialogReview.show(getSupportFragmentManager(), "dialog");
                    }
                }
            }
        });

        this.mButtonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogBooking alertDialogBooking = new AlertDialogBooking();

                alertDialogBooking.show(getSupportFragmentManager(), "dialogBooking");
            }
        });

        this.mButtonRefeicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogRefeicao alertDialogRefeicao = new AlertDialogRefeicao();

                alertDialogRefeicao.show(getSupportFragmentManager(), "dialogMeals");
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startLocationUpdates();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("idRestaurant")) {
                this.idRestaurant = Integer.parseInt(extras.getString("idRestaurant"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_fav, menu);

        this.favRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid())
                .whereEqualTo("idRestaurant", String.valueOf(this.idRestaurant))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    MenuItem item = menu.findItem(R.id.fav_restaurant);

                    item.setIcon(R.drawable.ic_baseline_favorite_24);

                    favOff = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fav_restaurant:
                if (!favOff) {
                    FavoriteFirestore favoriteFirestore = new FavoriteFirestore(this.mAuth.getCurrentUser().getUid(), String.valueOf(this.idRestaurant),
                            this.restaurant.getName(), this.restaurant.getLocation().getCity(), this.restaurant.getCuisines(), this.restaurant.getThumb(),
                            this.restaurant.getLocation().getLatitude(), this.restaurant.getLocation().getLongitude());

                    this.favRef.add(favoriteFirestore);
                    item.setIcon(R.drawable.ic_baseline_favorite_24);
                    favOff = true;
                } else {
                    this.favRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid())
                            .whereEqualTo("idRestaurant", String.valueOf(this.idRestaurant))
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            WriteBatch writeBatch = db.batch();

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                writeBatch.delete(documentSnapshot.getReference());
                            }

                            writeBatch.commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });

                    item.setIcon(R.drawable.ic_baseline_favorite_border_24_not_check);
                    favOff = false;
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        Restaurant newRestaurant = new Restaurant(this.restaurant.getName(), mAuth.getCurrentUser().getEmail(), this.restaurant.getLocation().getCity()
                , this.restaurant.getLocation().getAddress(), this.restaurant.getThumb());
        restaurantViewModel.insert(newRestaurant);

        startActivity(intent);
    }

    @Override
    public void addMeals(String meals) {
        MealsFirestore mealsFirestore = new MealsFirestore(this.restaurant.getId(), this.restaurant.getName(),
                this.mAuth.getCurrentUser().getUid(), meals, this.restaurant.getThumb());

        this.mealsRef.add(mealsFirestore);
    }
}
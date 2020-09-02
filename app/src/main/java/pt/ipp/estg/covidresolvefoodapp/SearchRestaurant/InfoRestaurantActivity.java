package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewAdapter;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogBooking;
import pt.ipp.estg.covidresolvefoodapp.AlertDialog.AlertDialogReview;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantViewModel;
import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.PerfilUser.UserReviewFragment;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
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

    private RestaurantInfoRetro restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        this.mAuth = FirebaseAuth.getInstance();

        restaurantViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(RestaurantViewModel.class);

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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
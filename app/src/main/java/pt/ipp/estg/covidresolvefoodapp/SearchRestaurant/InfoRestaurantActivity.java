package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

import pt.ipp.estg.covidresolvefoodapp.PerfilUser.UserReviewFragment;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoRestaurantActivity extends AppCompatActivity implements UserReviewFragment.OnFragmentUserReviewInteractionListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //TODO: Associa-las Porque senão temos 0-0
    private CollectionReference userRef = db.collection("User");
    private CollectionReference reviewRef = db.collection("Review");

    private Toolbar myToolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        int idRestaurant = Integer.parseInt(getIntent().getStringExtra("idRestaurant"));

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

        //TODO: Fazer o adapter
        //this.mRecyclerView = findViewById(R.id.mRecyclerview_show_reviews);

        UserReviewFragment userReviewFragment = new UserReviewFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_reviews_info_res_container, userReviewFragment);
        fragmentTransaction.commit();

        this.reviewRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    mButtonReview.setVisibility(View.INVISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Document Problem", Toast.LENGTH_SHORT).show();
            }
        });

        this.getAPIZomato().getRestaurant(idRestaurant).enqueue(new Callback<RestaurantInfoRetro>() {
            @Override
            public void onResponse(Call<RestaurantInfoRetro> call, Response<RestaurantInfoRetro> response) {
                RestaurantInfoRetro restaurant = response.body();

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

}
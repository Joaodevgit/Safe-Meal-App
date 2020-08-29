package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Iterator;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoRestaurantActivity extends AppCompatActivity {

    private Toolbar myToolbar;

    private TextView mTextName;
    private TextView mTextCity;
    private TextView mTextEstab;
    private TextView mTextNota;
    private TextView mTextAdress;
    private TextView mTextHorarios;
    private TextView mTextISTableBoooking;

    private Button mButtonReview;
    private Button mButtonBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        this.myToolbar = findViewById(R.id.toolbar_restaurant_show);
        setSupportActionBar(this.myToolbar);

        this.mTextEstab = findViewById(R.id.restaurant_establishment_info);

        this.getAPIZomato().getRestaurant(Integer.parseInt(getIntent().getStringExtra("idRestaurant"))).enqueue(new Callback<RestaurantInfoRetro>() {
            @Override
            public void onResponse(Call<RestaurantInfoRetro> call, Response<RestaurantInfoRetro> response) {

                Iterator<String> itr = response.body().getEstablishment().iterator();
                String contentEstab = "";

                while (itr.hasNext()) {
                    contentEstab += itr.next();
                }

                mTextEstab.setText(contentEstab);
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
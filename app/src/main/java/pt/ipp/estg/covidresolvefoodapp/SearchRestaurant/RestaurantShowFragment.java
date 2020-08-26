package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.RestaurantsRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantShowFragment extends Fragment {

    private TextView mTextView;

    public RestaurantShowFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurant_show, container, false);

        this.mTextView = view.findViewById(R.id.result_show);

        this.getAPIZomato().searchRestaurants(41.157944, -8.629105, "1", "16")
                .enqueue(new Callback<RestaurantsRetro>() {
                    @Override
                    public void onResponse(Call<RestaurantsRetro> call, Response<RestaurantsRetro> response) {
                        mTextView.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<RestaurantsRetro> call, Throwable t) {
                        System.out.println(call.request());
                    }
                });

        return view;
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
package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantsRetro;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.InfoRestaurantActivity;

public class ShowRestaurantsAdapter extends RecyclerView.Adapter<ShowRestaurantsAdapter.ShowRestaurantsViewHolder> {

    private Context mContext;
    private RestaurantsRetro mRestaurants;

    public ShowRestaurantsAdapter(Context mContext, RestaurantsRetro mRestaurants) {
        this.mContext = mContext;
        this.mRestaurants = mRestaurants;
    }

    @Override
    public ShowRestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_restaurant, parent, false);

        return new ShowRestaurantsViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ShowRestaurantsViewHolder viewHolder, int position) {
        final RestaurantInfoRetro restaurant = this.mRestaurants.getRestaurants().get(position).getRestaurant();

        ImageView imageRestaurant = viewHolder.imageRestaurant;
        if (!restaurant.getThumb().equals("")) {
            Picasso.get().load(restaurant.getThumb()).into(imageRestaurant);
        } else {
            Picasso.get().load("https://i.postimg.cc/zfX7My2F/tt.jpg").into(imageRestaurant);
        }


        TextView nameRestaurant = viewHolder.nameRestaurant;
        nameRestaurant.setText("Nome do restaurante: " + restaurant.getName());

        TextView cityRestaurant = viewHolder.cityRestaurant;
        cityRestaurant.setText("Cidade: " + restaurant.getLocation().getCity());

        RatingBar starsNotation = viewHolder.starsNotation;
        starsNotation.setRating(Float.parseFloat(restaurant.getUser_rating().getAggregate_rating()));

        TextView textNotation = viewHolder.textNotation;
        textNotation.setText("(" + restaurant.getUser_rating().getAggregate_rating() + ")");

        Button moreInfo = viewHolder.moreInfo;

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfoRestaurantActivity.class);
                intent.putExtra("idRestaurant", restaurant.getId());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mRestaurants.getRestaurants().size();
    }

    public class ShowRestaurantsViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageRestaurant;
        public TextView nameRestaurant;
        public TextView cityRestaurant;
        public RatingBar starsNotation;
        public TextView textNotation;
        public Button moreInfo;

        public ShowRestaurantsViewHolder(View itemView) {
            super(itemView);

            this.imageRestaurant = itemView.findViewById(R.id.image_restaurant);
            this.nameRestaurant = itemView.findViewById(R.id.restaurant_name);
            this.cityRestaurant = itemView.findViewById(R.id.restaurant_city);
            this.starsNotation = itemView.findViewById(R.id.estrelas);
            this.textNotation = itemView.findViewById(R.id.nota);
            this.moreInfo = itemView.findViewById(R.id.details_restaurant);
        }
    }
}

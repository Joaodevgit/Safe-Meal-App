package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.RestaurantsRetro;

public class ShowRestaurantsAdapter extends RecyclerView.Adapter<ShowRestaurantsAdapter.ShowRestaurantsViewHolder> {

    private Context mContext;
    private RestaurantsRetro mRestaurants;

    public ShowRestaurantsAdapter(Context mContext, RestaurantsRetro mRestaurants) {
        this.mContext = mContext;
        this.mRestaurants = mRestaurants;
    }

    @Override
    public ShowRestaurantsViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_restaurant, parent, false);

        return new ShowRestaurantsViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ShowRestaurantsViewHolder viewHolder, int position) {
        final RestaurantInfoRetro restaurant = this.mRestaurants.getRestaurants().get(position).getRestaurant();

        TextView textView = viewHolder.nameRestaurant;
        textView.setText(restaurant.getName());
    }

    @Override
    public int getItemCount() {
        return this.mRestaurants.getRestaurants().size();
    }

    public class ShowRestaurantsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameRestaurant;

        public ShowRestaurantsViewHolder(View itemView) {
            super(itemView);

            this.nameRestaurant = itemView.findViewById(R.id.restaurant_name);
        }
    }
}

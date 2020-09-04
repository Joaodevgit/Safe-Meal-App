package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import pt.ipp.estg.covidresolvefoodapp.Model.MealsFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserMealsAdapter extends FirestoreRecyclerAdapter<MealsFirestore, UserMealsAdapter.UserMealsViewHolder> {

    public UserMealsAdapter(FirestoreRecyclerOptions<MealsFirestore> options) {
        super(options);
    }

    @NonNull
    @Override
    public UserMealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View view = inflater.inflate(R.layout.item_user_meals, parent, false);

        // Return a new holder instance
        return new UserMealsAdapter.UserMealsViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull UserMealsViewHolder holder, int position, @NonNull MealsFirestore model) {

        ImageView imageRestaurant = holder.imageRestaurant;
        Picasso.get().load(model.getThumb()).into(imageRestaurant);


        TextView restaurantName = holder.restaurantName;
        restaurantName.setText("Restaurante: " + model.getRestaurantName());

        TextView contentMessage = holder.contentMessage;
        contentMessage.setText("Refeição escolhida: " + model.getContentMeals());

    }

    public class UserMealsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageRestaurant;
        private TextView restaurantName;
        private TextView contentMessage;

        public UserMealsViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageRestaurant = itemView.findViewById(R.id.image_restaurant_meals);
            this.restaurantName = itemView.findViewById(R.id.restaurant_name_meals);
            this.contentMessage = itemView.findViewById(R.id.meals_restaurant);
        }
    }
}

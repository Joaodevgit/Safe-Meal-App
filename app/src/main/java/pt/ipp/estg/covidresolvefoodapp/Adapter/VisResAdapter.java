package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;

public class VisResAdapter extends RecyclerView.Adapter<VisResAdapter.VisResViewHolder> {

    private Context mContext;
    private List<Restaurant> mVisRes;

    public VisResAdapter(Context mContext, List<Restaurant> mVisRes) {
        this.mContext = mContext;
        this.mVisRes = mVisRes;
    }

    public int getItemCount() {
        return mVisRes.size();
    }

    @Override
    public VisResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View visResViewHolder = inflater.inflate(R.layout.item_vis_res, parent, false);

        // Return a new holder instance
        return new VisResViewHolder(visResViewHolder);
    }

    @Override
    public void onBindViewHolder(VisResViewHolder viewHolder, int position) {
        // Get the data model based on position
        Restaurant visRes = mVisRes.get(position);

        // Set name
        TextView textView = viewHolder.visResNameTxtView;
        textView.setText(visRes.getName());

    }

    public class VisResViewHolder extends RecyclerView.ViewHolder {

        public TextView visResNameTxtView;

        public VisResViewHolder(View itemView) {
            super(itemView);
            visResNameTxtView = itemView.findViewById(R.id.visResName);
        }
    }
}

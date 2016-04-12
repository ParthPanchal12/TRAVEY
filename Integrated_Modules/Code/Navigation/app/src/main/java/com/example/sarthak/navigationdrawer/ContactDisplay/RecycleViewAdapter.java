package com.example.sarthak.navigationdrawer.ContactDisplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sarthak on 2/10/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> implements Filterable {
    private static Context ctx;
    public ArrayList<Friends> friends;
    private ArrayList<Friends> all_friends;
    private LayoutInflater inflater;
    private CustomFilterable filter;

    public RecycleViewAdapter(Context ctx, ArrayList<Friends> friends) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(this.ctx);
        this.friends = friends;
        all_friends=friends;
    }

    public RecycleViewAdapter() {

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_friends_information_display, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.setName(friends.get(position).getName());
        holder.setPhone(friends.get(position).getPhone());
        String title = friends.get(position).getName();
        if (title == null || title.length() == 0)
            title = "X";
        holder.setDrawable(TextDrawable.builder()
                .buildRound("" + title.toUpperCase().charAt(0), getRandomColor()));
        holder.setTextDrawable();

    }

    public int getRandomColor() {
        int color[] = {
                Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN,
                Color.MAGENTA, Color.RED, Color.YELLOW};
        Random random = new Random();
        return color[random.nextInt(color.length - 1)];
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilterable();
        }
        return filter;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView name;
        private TextView phone;
        private TextDrawable drawable;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = (TextView) itemView.findViewById(R.id.title);
            phone = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public TextDrawable getDrawable() {
            return drawable;
        }

        public void setDrawable(TextDrawable drawable) {
            this.drawable = drawable;
        }

        public String getName() {
            return name.getText().toString();
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public String getPhone() {
            return phone.getText().toString();
        }

        public void setPhone(String phone) {
            this.phone.setText(phone);
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

        public void setTextDrawable() {
            this.image.setImageDrawable(drawable);
        }



    }

    class CustomFilterable extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Friends> filtered_adapter_results = new ArrayList<>();
                int friends_size = all_friends.size();
                Log.d("here","here");
                for (int i = 0; i < friends_size; i++) {
                    if (all_friends.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filtered_adapter_results.add(all_friends.get(i));
                    }
                }
                filterResults.count = filtered_adapter_results.size();
                filterResults.values = filtered_adapter_results;
            } else {
                filterResults.count = all_friends.size();
                filterResults.values =all_friends;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           friends = (ArrayList<Friends>) results.values;
            notifyDataSetChanged();
        }
    }
}

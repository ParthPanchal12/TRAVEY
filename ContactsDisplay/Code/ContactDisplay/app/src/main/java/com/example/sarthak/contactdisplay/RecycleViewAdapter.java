package com.example.sarthak.contactdisplay;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sarthak on 2/10/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> {
    private static Context ctx;
    private ArrayList<Friends> friends;
    private LayoutInflater inflater;
    public static ContextMenu.ContextMenuInfo info;

    public RecycleViewAdapter(Context ctx, ArrayList<Friends> friends) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(this.ctx);
        this.friends = friends;
    }

    public RecycleViewAdapter() {

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friends_information_display_adapter, parent, false);
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

    class CustomViewHolder extends RecyclerView.ViewHolder {
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
}

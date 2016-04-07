package com.example.sarthak.navigationdrawer.LeaderBoard;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sarthak.navigationdrawer.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sarthak on 2/10/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> {
    private static Context ctx;
    private ArrayList<User> users;
    private LayoutInflater inflater;
    public static ContextMenu.ContextMenuInfo info;

    public RecycleViewAdapter(Context ctx, ArrayList<User> users) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(this.ctx);
        this.users = users;
    }

    public RecycleViewAdapter() {

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_leaderboard, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.setName(users.get(position).getName());
        holder.setUpvote(users.get(position).getUpvote());
        holder.setDownvote(users.get(position).getDownvote());
        String title = users.get(position).getName();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView name;
        private TextView downvote;
        private TextView upvote;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = (TextView) itemView.findViewById(R.id.title_name_user_leaderboard);
            upvote = (TextView) itemView.findViewById(R.id.upvoteText);
            downvote = (TextView) itemView.findViewById(R.id.downvoteText);
            image = (ImageView) itemView.findViewById(R.id.image_view_user_leaderboard);
        }

        public String getName() {
            return name.getText().toString();
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public int getDownvote() {
            return Integer.valueOf(downvote.getText().toString().trim());
        }

        public void setDownvote(int downvote) {
            this.downvote.setText(String.valueOf(downvote));
        }

        public int getUpvote() {
            return Integer.valueOf(upvote.getText().toString().trim());
        }

        public void setUpvote(int upvote) {
            this.upvote.setText(String.valueOf(upvote));
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

    }
}

package com.example.sarthak.profilepage_travey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sarthak on 19/3/16.
 */
public class Profile_RecyclerViewAdapter extends RecyclerView.Adapter<Profile_RecyclerViewAdapter.CustomAdapter> {

    private ArrayList<ProfileClass> profile;
    private Context context;

    public Profile_RecyclerViewAdapter(ArrayList<ProfileClass> profile, Context context) {
        this.profile = profile;
        this.context = context;
    }

    @Override
    public Profile_RecyclerViewAdapter.CustomAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_personal_info_adapter, parent, false);
        return new CustomAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(Profile_RecyclerViewAdapter.CustomAdapter holder, int position) {
        if (holder != null) {
            holder.setUserInfoDescription(profile.get(position).getDescription());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), profile.get(position).getIcon());
            holder.setUserInfoIcon(bitmap);
            holder.setUserInfoTitle(profile.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return profile.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class CustomAdapter extends RecyclerView.ViewHolder {

        private TextView userInfoTitle;
        private ImageView userInfoIcon;
        private TextView userInfoDescription;

        public CustomAdapter(View itemView) {
            super(itemView);
            userInfoDescription = (TextView) itemView.findViewById(R.id.textView_UserInfoDescription);
            userInfoIcon = (ImageView) itemView.findViewById(R.id.imageView_UserInfoIcon);
            userInfoTitle = (TextView) itemView.findViewById(R.id.textView_UserInfoTitle);
        }

        public String getUserInfoTitle() {
            return userInfoTitle.getText().toString();
        }

        public void setUserInfoTitle(String userInfoTitle) {
            this.userInfoTitle.setText(userInfoTitle);
        }

        public ImageView getUserInfoIcon() {
            return userInfoIcon;
        }

        public void setUserInfoIcon(Bitmap userInfoIcon) {
            this.userInfoIcon.setImageBitmap(userInfoIcon);
        }

        public String getUserInfoDescription() {
            return userInfoDescription.getText().toString();
        }

        public void setUserInfoDescription(String userInfoDescription) {
            this.userInfoDescription.setText(userInfoDescription);
        }
    }
}

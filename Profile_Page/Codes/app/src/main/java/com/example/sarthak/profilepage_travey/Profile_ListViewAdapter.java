package com.example.sarthak.profilepage_travey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sarthak on 19/3/16.
 */
public class Profile_ListViewAdapter extends BaseAdapter {
    private ArrayList<ProfileClass> profile;
    private Context context;
    public Profile_ListViewAdapter(ArrayList<ProfileClass> profile, Context context) {
        this.profile = profile;
        this.context = context;
    }

    @Override
    public int getCount() {
        /*Number of parameters in ProfileClass except ProfilePic*/
        return profile.size();
    }

    @Override
    public Object getItem(int position) {
        return profile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView userInfoTitle;
        ImageView userInfoIcon;
        TextView userInfoDescription;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listview_personal_info_adapter,parent,false);
        }
        /*Setting icons and text of each field in the listview*/
        userInfoIcon=(ImageView)convertView.findViewById(R.id.imageView_UserInfoDescription);
        userInfoDescription=(TextView)convertView.findViewById(R.id.textView_UserInfoDescription);
        userInfoTitle=(TextView)convertView.findViewById(R.id.textView_UserInfoTitle);
        userInfoDescription.setText(profile.get(position).getDescription());
        userInfoTitle.setText(profile.get(position).getTitle());
        Bitmap icon= BitmapFactory.decodeResource(context.getResources(),profile.get(position).getIcon());
        userInfoIcon.setImageBitmap(icon);
        /*To set icon*/
        return convertView;
    }
}

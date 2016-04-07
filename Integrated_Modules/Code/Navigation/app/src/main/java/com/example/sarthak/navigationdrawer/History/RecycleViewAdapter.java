package com.example.sarthak.navigationdrawer.History;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sarthak.navigationdrawer.Backend.Backend.History;
import com.example.sarthak.navigationdrawer.R;

import java.util.ArrayList;

/**
 * Created by Sarthak on 2/10/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> {
    private static Context ctx;
    private ArrayList<History> histories;
    private LayoutInflater inflater;

    public RecycleViewAdapter(Context ctx, ArrayList<History> histories) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(this.ctx);
        this.histories = histories;
    }

    public RecycleViewAdapter() {

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_history, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.setSource(histories.get(position).getSource());
        holder.setDestination(histories.get(position).getDestination());
        holder.setDate(histories.get(position).getDate());
    }



    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return histories.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView source;
        private TextView destination;
        private TextView date;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.source = (TextView) itemView.findViewById(R.id.source_history);
            this.destination = (TextView) itemView.findViewById(R.id.destination_history);
            this.date = (TextView) itemView.findViewById(R.id.date_history);
        }

        public String getSource() {
            return this.source.getText().toString();
        }

        public void setSource(String source) {
            this.source.setText(source);
        }

        public String getDestination() {
            return destination.getText().toString();
        }

        public void setDestination(String destination) {
            this.destination.setText(destination);
        }

        public String getDate() {
            return date.getText().toString();
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

    }
}

package com.example.android.spotifanni;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class VideoAdapterRecycler extends RecyclerView.Adapter<VideoAdapterRecycler.MyViewHolder> {

    private List<VideoItem> videos;
    public OnItemClickListener listener;
    public Activity currentActivity;

    public VideoAdapterRecycler(List<VideoItem> videos, OnItemClickListener listener, Activity currentActivity) {
        this.videos = videos;
        this.listener = listener;
        this.currentActivity = currentActivity;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView titleView;
        public ImageView heartIcon;
        public ImageView addView;
        public ImageView orderView;
        public View view;

        public MyViewHolder(View v) {
            super(v);
            titleView = (TextView) itemView.findViewById(R.id.title_id);
            heartIcon = (ImageView) itemView.findViewById(R.id.icon_id);
            addView = (ImageView) itemView.findViewById(R.id.add_id);
            orderView = (ImageView) itemView.findViewById(R.id.order_id);
            this.view = v;
        }

        public void bind(final VideoItem video, final OnItemClickListener listener) {

            this.titleView.setText(video.getTitle());
            if (video.isFavourite())
                this.heartIcon.setImageResource(R.drawable.ic_heart_black);
            else
                this.heartIcon.setImageResource(R.drawable.ic_heart_white);

            this.orderView.setImageResource(R.drawable.ic_order_black_16dp);
            this.addView.setImageResource(R.drawable.ic_add);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(video);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener.onLongClicked(v, video);
                }
            });

            heartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (video.isFavourite())
                        heartIcon.setImageResource(R.drawable.ic_heart_white);
                    else
                        heartIcon.setImageResource(R.drawable.ic_heart_black);
                    listener.onHearthClick(v, video);
                }
            });

            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddClick(v, video);

                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoAdapterRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VideoItem video = this.videos.get(position);
        holder.bind(video, listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videos.size();
    }

}


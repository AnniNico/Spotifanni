package com.example.android.spotifanni;

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
        private List<VideoItem> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titleView;
            public ImageView iconView;
            public ImageView addView;
            public ImageView orderView;
            public MyViewHolder(View v) {
                super(v);
                titleView= (TextView) itemView.findViewById(R.id.title_id);
                iconView= (ImageView) itemView.findViewById(R.id.icon_id);
                addView= (ImageView) itemView.findViewById(R.id.icon_id);
                orderView= (ImageView) itemView.findViewById(R.id.icon_id);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public VideoAdapterRecycler(List<VideoItem> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public VideoAdapterRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_item, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            VideoItem video = mDataset.get(position);
            holder.titleView.setText(video.getTitle());
            if(video.isFavourite())
                holder.iconView.setImageResource(R.drawable.ic_heart_black);
            else
                holder.iconView.setImageResource(R.drawable.ic_heart_white);

            holder.orderView.setImageResource(R.drawable.ic_order_black_16dp);
            holder.addView.setImageResource(R.drawable.ic_heart_white);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }


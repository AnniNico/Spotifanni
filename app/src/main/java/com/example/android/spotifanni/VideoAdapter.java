package com.example.android.spotifanni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class VideoAdapter extends ArrayAdapter<VideoItem> {

    public VideoAdapter(Context context, int resource, List<VideoItem> videos) {
        super(context, resource, videos);
    }

    @Override
    public void add(VideoItem video) {
        this.add(video);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.song_item, null);
        TextView title = (TextView)convertView.findViewById(R.id.songItemTitle);
        title.setText(getItem(position).getTitle());

        return convertView;
    }
}

package com.example.android.spotifanni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(Context context, int resource, ArrayList<Song> songs) {
        super(context, resource, songs);
    }

    @Override
    public void add(Song song) {
        this.add(song);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.song_item, null);
        TextView songTitle = (TextView)convertView.findViewById(R.id.songItemTitle);
        songTitle.setText(getItem(position).getTitle());
        TextView author = (TextView)convertView.findViewById(R.id.songItemAuthor);
        author.setText(getItem(position).getAuthor());
        return convertView;
    }
}

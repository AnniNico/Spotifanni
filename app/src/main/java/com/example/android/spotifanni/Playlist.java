package com.example.android.spotifanni;

import java.util.ArrayList;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class Playlist {

    private String name;
    private ArrayList<Song> songs;
    private boolean playing;
    private int order;

    public Playlist(String name,  ArrayList<Song> songs) {
        this.name = name;
        this.songs = songs;
    }

    public Playlist() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

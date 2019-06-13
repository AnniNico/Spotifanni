package com.example.android.spotifanni;

import java.util.ArrayList;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class Playlist {

    private String name;
    private ArrayList<VideoItem> songs;
    private boolean playing;
    private int order;

    public Playlist(String name,  ArrayList<VideoItem> songs) {
        this.name = name;
        this.songs = songs;
    }

    public Playlist() {
        songs = new ArrayList<>();
    }

    public Playlist(String name) {
        this.name = name;
        songs = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VideoItem> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<VideoItem> songs) {
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

    public boolean contains(VideoItem video){
        for(VideoItem s : songs)
        {
            if(s.getTitle().equals(video.getTitle()))
                return true;
        }
        return false;
    }

    public void removeSong(VideoItem video) {
        for(int i = 0 ; i< songs.size(); i++){
            if(songs.get(i).getTitle().equals(video.getTitle()))
                songs.remove(i);
        }
    }
}

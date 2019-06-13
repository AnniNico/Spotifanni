package com.example.android.spotifanni;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by nicolo.mandrile on 09/06/2019.
 * Singleton class that contains all my playlists.
 * Used to be shared between all application classes.
 * Contains my playlists and some utilities methods.
 * Manages all the operations on the playlists
 */

public class Playlists {

    private static Playlists instance;
    private MainActivity activity;

    public static Playlists getInstance(MainActivity activity) {
        if (instance == null) {
            MyXMLManager xml = new MyXMLManager();
            HashMap<String, Playlist> parsedPlaylists = new HashMap<>();
            try {
                parsedPlaylists = xml.parse(new FileInputStream(new File(activity.getFilesDir(), "playlists.xml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            instance = new Playlists();
            instance.activity = activity;
            instance.add(parsedPlaylists);
        }
        return instance;
    }

    private HashMap<String, Playlist> playlists;

    private Playlists() {
        playlists = new HashMap<>();
    }

    public HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

    public void add(HashMap<String, Playlist> parsedPlaylists) {
        playlists.putAll(parsedPlaylists);
        saveInternalStorageFile();
    }

    public boolean add(Playlist playlistToAdd) {
        if (playlistToAdd != null && playlistToAdd.getName() != null && !playlists.containsKey(playlistToAdd.getName())) {
            playlists.put(playlistToAdd.getName(), playlistToAdd);
            saveInternalStorageFile();
            return true;
        }
        return false;
    }


    public Playlist getPlayingPlaylist() {

        Playlist playing = playlists.get(Config.FAVOURITE_SONGS);

        Collection<Playlist> values = playlists.values();
        for (Playlist pair : values) {
            if (pair.isPlaying())
                return pair;
        }

        return playing;
    }


    public void saveInternalStorageFile() {
        //update internal storage file
        MyXMLManager myXMLManager = new MyXMLManager();
        myXMLManager.setPlaylists(playlists);
        File file = new File(activity.getFilesDir(), "playlists.xml");
        myXMLManager.writeXml(file, playlists);

        activity.setUpPlaylistsMenu();
        //activity.reloadVideoList(getPlayingPlaylist());
    }

    public void deletePlaylist(String name) {
        playlists.remove(name);
        activity.reloadVideoList(getPlayingPlaylist());
        saveInternalStorageFile();
    }

    public boolean addSongToPlaylist(VideoItem video, String playlistTitle) {
        Playlist pl = playlists.get(playlistTitle);
        if (pl != null) {
            if (playlistTitle.equals(activity.getString(R.string.favourite_songs))) {
                video.setFavourite(true);
                //set this song as favourite
                Collection<Playlist> values = playlists.values();
                for (Playlist pair : values) {
                    for (VideoItem v : pair.getSongs()) {
                        if (v.getTitle().equals(video.getTitle()))
                            v.setFavourite(true);
                    }
                    playlists.put(pair.getName(), pair);
                }
            }
            if(!pl.contains(video))
            {
                pl.getSongs().add(video);
                playlists.put(playlistTitle, pl);
                return true;
            }
            return false;
        }
        return false;
    }

    public void removeSongToPlaylist(VideoItem video, String playlistTitle) {
        Playlist pl = playlists.get(playlistTitle);
        if (pl != null) {
            //if a song is removed from favourites, than i set isFavourite = false of the
            //same song in others playlists
            if (playlistTitle.equals(activity.getString(R.string.favourite_songs))) {
                video.setFavourite(false);
                //set this song as favourite
                Collection<Playlist> values = playlists.values();
                for (Playlist pair : values) {
                    for (VideoItem v : pair.getSongs()) {
                        if (v.getTitle().equals(video.getTitle()))
                            v.setFavourite(false);
                    }
                    playlists.put(pair.getName(), pair);
                }
            }
            pl.removeSong(video);
        }
        //activity.reloadVideoList(playlists.get(playlistTitle));
        saveInternalStorageFile();

    }

    public void renamePlaylist(String oldPlaylistTitle, String newPlaylistTitle) {
        Playlist pl = playlists.get(oldPlaylistTitle);
        playlists.remove(oldPlaylistTitle);
        pl.setName(newPlaylistTitle);
        playlists.put(newPlaylistTitle, pl);
        saveInternalStorageFile();
    }

}

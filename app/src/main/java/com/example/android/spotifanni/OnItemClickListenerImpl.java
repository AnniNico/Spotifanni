package com.example.android.spotifanni;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by nicolo.mandrile on 09/06/2019.
 * Class used to handle RecyclerView Items events.
 */

public class OnItemClickListenerImpl implements OnItemClickListener {

    MainActivity activity;

    public OnItemClickListenerImpl(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(VideoItem video) {
        System.out.println("CLICK");
        System.out.println("Reproduce video: " + video.getTitle());
    }

    @Override
    public void onHearthClick(View v, VideoItem video) {

        Playlist favourites = null;
        HashMap<String, Playlist> playlists = Playlists.getInstance(activity).getPlaylists();
        if (playlists.size() == 0) {
            favourites = new Playlist();
            favourites.setName(activity.getString(R.string.favourite_songs));
            favourites.setPlaying(true);
            favourites.setOrder(0);
            Playlists.getInstance(activity).add(favourites);
        } else
            favourites = playlists.get(activity.getString(R.string.favourite_songs));

        //if the playlist does not contain the song, the song is added
        if (!favourites.contains(video)) {
            Playlists.getInstance(activity).addSongToPlaylist(video, activity.getString(R.string.favourite_songs));
            Context context = activity.getApplicationContext();
            Toast.makeText(context, context.getString(R.string.added), Toast.LENGTH_SHORT).show();
        } else //removed
        {
            Context context = activity.getApplicationContext();
            Toast.makeText(context, context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
            if (favourites != null)
            {
                favourites.removeSong(video);
                Playlists.getInstance(activity).removeSongToPlaylist(video, activity.getString(R.string.favourite_songs).toString());
                TextView tw = (TextView) activity.findViewById(R.id.currentPlaylistTitle);
                //activity.reloadVideoList(Playlists.getInstance(activity).getPlaylists().get(tw.getText().toString()));
            }
        }

    }

    @Override
    public boolean onLongClicked(View v, VideoItem item) {
        System.out.println("LONG");
        //TODO change order
        return false;
    }

    @Override
    public void onAddClick(final View v, final VideoItem video) {

        //creating a popup  menu
        final PopupMenu popup = new PopupMenu(activity, v);
        Menu menu = popup.getMenu();
        //id -3 remove song
        menu.add(-1,-3, 0, activity.getString(R.string.menu_delete));
        SubMenu subMenu = menu.addSubMenu(activity.getString(R.string.menu_add));
        int order = 0;
        //id -1 add
        subMenu.add(0, -1, order++, activity.getString(R.string.create_palylist)).setIcon(R.drawable.ic_add);

        Collection<Playlist> pl = Playlists.getInstance(activity).getPlaylists().values();
        for (Playlist pair : pl) {
            if (!pair.getName().equals(activity.getString(R.string.favourite_songs))) {
                subMenu.add(0, ++order, order, pair.getName()).setIcon(R.drawable.ic_playlist);
            } else
            {
                //id -2 favourites
                //subMenu.add(0, -2, 0, pair.getName()).setIcon(R.drawable.ic_heart_white);
            }

        }
        //inflating menu from xml resource
        popup.inflate(R.menu.song_options_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final String s = (String) item.getTitle();
                final Context context = activity.getApplicationContext();

                switch (item.getItemId()) {
                    case 0:
                        break;
                    case -1:
                        //add new playlist
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.create_palylist));
                        final EditText input = new EditText(activity);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newPlaylistTitle = input.getText().toString();
                                Playlist newPlaylist = new Playlist(newPlaylistTitle);
                                newPlaylist.getSongs().add(video);

                                if(!Playlists.getInstance(activity).add(newPlaylist))
                                {
                                    Toast.makeText(context, context.getString(R.string.plalylist_already_exists), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.show();
                        break;
                    /*case -2:
                        //add to favourites
                        onHearthClick(v,video);
                        break;*/
                    case -3:
                        //remove
                        TextView tw = (TextView) activity.findViewById(R.id.currentPlaylistTitle);
                        Playlists.getInstance(activity).removeSongToPlaylist(video, tw.getText().toString());
                        Toast.makeText(context, context.getString(R.string.song_removed), Toast.LENGTH_SHORT).show();
                        activity.reloadVideoList( Playlists.getInstance(activity).getPlaylists().get(tw.getText().toString()));
                        break;
                    default:
                        //add to playlist
                        if(Playlists.getInstance(activity).addSongToPlaylist(video, item.getTitle().toString()))
                            Toast.makeText(context, context.getString(R.string.song_added), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getString(R.string.song_already_addedd), Toast.LENGTH_SHORT).show();

                }
                return true;
            }

        });
        //displaying the popup
        popup.show();
    }

}

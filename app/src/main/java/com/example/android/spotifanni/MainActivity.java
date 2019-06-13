package com.example.android.spotifanni;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Set menu options - Add all saved playlists
        setUpPlaylistsMenu();
        Playlist playingPlaylist = Playlists.getInstance(this).getPlayingPlaylist();
        reloadVideoList(playingPlaylist);
    }



    public void setUpPlaylistsMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menu.clear();
        menu.add(0, 0, 0, getString(R.string.favourite_songs)).setIcon(R.drawable.ic_heart_black);
        SubMenu subMenu = menu.addSubMenu("Playlist");
        int i = 0;
        Collection<Playlist> pl = Playlists.getInstance(this).getPlaylists().values();
        for (Playlist pair : pl) {
            if (!pair.getName().equals(getString(R.string.favourite_songs))) {
                subMenu.add(0, i++, 0, pair.getName()).setIcon(R.drawable.ic_playlist);
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search song");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Playlist playlist;
        if (id == R.id.favourites)
            playlist = Playlists.getInstance(this).getPlaylists().get(Config.FAVOURITE_SONGS);
        else
            playlist = Playlists.getInstance(this).getPlaylists().get(item.getTitle());

        reloadVideoList(playlist);
        return true;
    }

    public void reloadVideoList(Playlist playlist) {
        if(playlist==null || playlist.getSongs()==null)
            return;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.currentPlaylistItems);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new VideoAdapterRecycler(playlist.getSongs(), new OnItemClickListenerImpl(this),this);
        recyclerView.setAdapter(mAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(playlist.getName());
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        if(!title.getText().equals(getString(R.string.favourite_songs)))
        {
            final MainActivity act = this;
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit, 0);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(act, findViewById(R.id.currentPlaylistTitle));
                    Menu menu = popup.getMenu();

                    // subMenu = menu.addSubMenu("Operazioni");
                    menu.add(0,1, 1, getString(R.string.rename_playlist));
                    menu.add(0,2, 2, getString(R.string.delete_playlist));

                    //inflating menu from xml resource
                    popup.inflate(R.menu.song_options_menu);
                    //adding click listene
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String s = (String) item.getTitle();
                            final Context context = getApplicationContext();

                            switch (item.getItemId()) {
                                case 1:
                                    //Rename playlist
                                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                                    builder.setTitle(getString(R.string.rename_palylist));
                                    final EditText input = new EditText(context);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(input);
                                    // Set up the buttons
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String newPlaylistTitle = input.getText().toString();
                                            TextView tw = (TextView) findViewById(R.id.currentPlaylistTitle);
                                            String oldPlaylistTitle = tw.getText().toString();
                                            Playlists.getInstance(null).renamePlaylist(oldPlaylistTitle, newPlaylistTitle);
                                            //tw.setText(newPlaylistTitle);
                                            Toast.makeText(context, context.getString(R.string.palylist_renamed), Toast.LENGTH_SHORT).show();
                                            reloadVideoList(Playlists.getInstance(null).getPlaylists().get(newPlaylistTitle));
                                        }
                                    });

                                    builder.show();
                                    break;
                                case 2:
                                    TextView tw = (TextView) findViewById(R.id.currentPlaylistTitle);
                                    if(tw.getText().toString().equals(R.string.favourite_songs))
                                    {
                                        Toast.makeText(context, context.getString(R.string.cannot_delete_fav), Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    else {
                                        Playlists.getInstance(null).deletePlaylist(tw.getText().toString());
                                        reloadVideoList(Playlists.getInstance(null).getPlaylists().get(R.string.favourite_songs));
                                        Toast.makeText(context, context.getString(R.string.palylist_deleted), Toast.LENGTH_SHORT).show();
                                        setUpPlaylistsMenu();
                                    }
                                    break;
                            }
                            return true;

                        };
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        //get user input and search on youtube
        try {
            YoutubeManager ym = new YoutubeManager(MainActivity.this, this);
            ym.execute(query);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // close search menu after search success
        if (mMenu != null) {
            (mMenu.findItem(R.id.action_search)).collapseActionView();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //TODO SHOW SUGGESTIONS OR HISTORY
        return false;
    }

}

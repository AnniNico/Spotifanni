package com.example.android.spotifanni;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener {

    private ArrayList<Playlist> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Save playlist on internal storate
        //savePlaylistInInternalStorage();

        //Read playlist from internal storaged xml file
        MyXMLManager xml = new MyXMLManager();
        try {
            //create my list
            playlists = xml.parse(new FileInputStream(new File(getFilesDir(), "playlists.xml")));
            //Set menu options - Add all saved playlists
            setUpPlaylistsMenu();
            setupSelectedPlaylist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupSelectedPlaylist() {

        ListView lv = (ListView) findViewById(R.id.currentPlaylistItems);
        ArrayList<VideoItem> songs = null;
        Playlist playingPlaylist = playlists.get(0);
        for (Playlist p : playlists) {
            if (p.isPlaying())
            {
                songs = p.getSongs();
                playingPlaylist = p;
            }
        }
        if (songs == null)
            songs = playlists.get(0).getSongs();

        /*final VideoAdapter adapter = new VideoAdapter(this, R.layout.song_item, songs);
        lv.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(R.string.favourite_songs);*/
        reloadVideoList(this, playingPlaylist);

    }

    public void savePlaylistInInternalStorage() {

        MyXMLManager xml = new MyXMLManager();
        xml.setPlaylists(playlists);
        File file = new File(getFilesDir(), "playlists.xml");
        xml.writeXml(file, playlists);

    }


    public void setUpPlaylistsMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        SubMenu subMenu = menu.addSubMenu("Playlist");
        int i = 0;
        for (Playlist p : playlists) {
            if (!p.getName().equals(getString(R.string.favourite_songs))) {
                subMenu.add(0, i++, 0, p.getName()).setIcon(R.drawable.ic_playlist);
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

    private Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu=menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search song");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ArrayList<VideoAdapter> songs = new ArrayList<VideoAdapter>();
        Playlist playlist;
        if (id == R.id.favourites)
            playlist = playlists.get(0);
        else
            playlist = playlists.get(id + 1);

        reloadVideoList(this, playlist);
        return true;
    }

    private void reloadVideoList(MainActivity mainActivity, Playlist playlist) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.currentPlaylistItems);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new VideoAdapterRecycler(playlist.getSongs());
        recyclerView.setAdapter(mAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(playlist.getName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListView lv = (ListView) findViewById(R.id.currentPlaylistItems);
        for (int i = 0; i < lv.getAdapter().getCount(); i++) {
            VideoItem song = (VideoItem) lv.getAdapter().getItem(i);
            if (song.isPlaying()) {
                //TODO Stop the music
                //and put the play icon
                Log.i("STOP!", "You clicked: " + song.getTitle() + " at position:" + i);
                song.setPlaying(false);
            } else {
                if (i == position) {
                    //TODO: PLAY THE SONG
                    //and put the play icon
                    Log.i("START!", "You clicked: " + song.getTitle() + " at position:" + position);
                    song.setPlaying(true);
                }

            }
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

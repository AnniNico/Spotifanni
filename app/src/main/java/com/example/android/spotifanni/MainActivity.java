package com.example.android.spotifanni;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setupSelectedPlaylist(){

        ListView lv = (ListView)  findViewById(R.id.currentPlaylistItems);
        ArrayList<Song> songs = null;
        for(Playlist p : playlists)
        {
            if(p.isPlaying())
                songs = p.getSongs();
        }
        if(songs==null)
            songs = playlists.get(0).getSongs();

        final SongAdapter adapter = new SongAdapter(this, R.layout.song_item, songs);
        lv.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(R.string.favourite_songs);


    }

    public void savePlaylistInInternalStorage(){

        //Prepare main page with favourite songs
        /*ArrayList<Playlist> playlists = new ArrayList<>();
        Playlist favourites = setupFavourites();
        playlists.add(favourites);
        playlists.add(setupFavouritesIndi());*/
        MyXMLManager xml = new MyXMLManager();
        xml.setPlaylists(playlists);
        File file = new File(getFilesDir(), "playlists.xml");
        xml.writeXml(file, playlists);

    }


    public void setUpPlaylistsMenu(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        SubMenu subMenu = menu.addSubMenu("Playlist");
        int i=0;
        for(Playlist p : playlists)
        {
            if(!p.getName().equals(getString(R.string.favourite_songs))) {
               subMenu.add(0, i++, 0, p.getName()).setIcon(R.drawable.ic_playlist);
            }
        }
    }
    /*public Playlist setupFavourites()
    {
        ListView lv = (ListView)  findViewById(R.id.currentPlaylistItems);
        final ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(new Song("Cosa sarà", "", "Lucio Dalla"));
        songs.add(new Song("Hold back the river", "", "James Bay"));
        songs.add(new Song("Pesto", "", "Calcutta"));
        songs.add(new Song("Luci a San Siro", "", "Vecchioni"));
        songs.add(new Song("Vai con Dio", "", "Coez"));
        final SongAdapter adapter = new SongAdapter(this, R.layout.song_item, songs);
        lv.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(R.string.favourite_songs);

        Playlist favourites = new Playlist(getString(R.string.favourite_songs),songs);
        return favourites;
    }
    public Playlist setupFavouritesIndi()
    {
        //TODO remove
        ListView lv = (ListView)  findViewById(R.id.currentPlaylistItems);
        final ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(new Song("Verdura", "", "Pinguini tattici nucelari"));
        songs.add(new Song("Punk", "", "Gazzelle"));
        songs.add(new Song("Pesto", "", "Calcutta"));
        final SongAdapter adapter = new SongAdapter(this, R.layout.song_item, songs);
        lv.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(R.string.favourite_songs);

        Playlist indie = new Playlist(getString(R.string.favourite_songs),songs);
        indie.setName("Indie");
        return indie;
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        ArrayList<Song> songs = new ArrayList<Song>();
        Playlist playlist ;
        if(id==R.id.favourites)
            playlist = playlists.get(0);
        else
            playlist = playlists.get(id+1);

        final SongAdapter adapter = new SongAdapter(this, R.layout.song_item, playlist.getSongs());
        final ListView listView = (ListView)  findViewById(R.id.currentPlaylistItems);
        listView.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.currentPlaylistTitle);
        title.setText(item.getTitle());
        listView.setClickable(true);
        listView.setOnItemClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListView lv = (ListView)  findViewById(R.id.currentPlaylistItems);
        for (int i=0;i<lv.getAdapter().getCount();i++){
            Song song = (Song) lv.getAdapter().getItem(i);
            if(song.isPlaying()){
                //TODO Stop the music
                //and put the play icon
                Log.i("STOP!", "You clicked: " + song.getTitle() + " at position:" + i);
                song.setPlaying(false);
            }
            else
            {
                if(i==position){
                    //TODO: PLAY THE SONG
                    //and put the play icon
                    Log.i("START!", "You clicked: " + song.getTitle() + " at position:" + position);
                    song.setPlaying(true);
                }

            }
        }
    }
}

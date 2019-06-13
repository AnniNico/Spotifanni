package com.example.android.spotifanni;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolo.mandrile on 27/05/2019.
 */

public class YoutubeManager  extends AsyncTask<String, Void, List<VideoItem>> {

    private YouTube youtube;
    //returned from search
    private YouTube.Search.List query;
    //maximum results that should be downloaded via the YouTube data API at a time
    private static final long MAXRESULTS = 25;

    WeakReference<MainActivity> mWeakActivity;

    //Constructor to properly initialize Youtube's object
    public YoutubeManager(Context context, MainActivity currentActivity) throws IOException {

        mWeakActivity = new WeakReference<MainActivity>(currentActivity);
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest request) throws IOException {
                //setting package name and sha1 certificate to identify request by server
                //request.getHeaders().set("X-Android-Package", Config.PACKAGENAME);
                //request.getHeaders().set("X-Android-Cert", Config.SHA1);
            }
        }).setApplicationName("Spotifanni").build();

        query = youtube.search().list("id,snippet");
        query.setKey(Config.KEY);
        query.setType("video");
        //setting fields which should be returned
        query.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");

    }


    public ArrayList<VideoItem> search(String keywords) throws IOException {

        //setting keyword to query
        query.setQ(keywords);
        query.setMaxResults(MAXRESULTS);

        SearchListResponse response = query.execute();
        List<SearchResult> results = response.getItems();
        List<VideoItem> items = new ArrayList<VideoItem>();
        if (results != null) {
            for(SearchResult singleVideo : results){
                ResourceId rId = singleVideo.getId();
                if (rId.getKind().equals("youtube#video")) {
                    VideoItem item = new VideoItem();
                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();
                    item.setId(singleVideo.getId().getVideoId());
                    item.setTitle(singleVideo.getSnippet().getTitle());
                    item.setDescription(singleVideo.getSnippet().getDescription());
                    item.setThumbnailURL(thumbnail.getUrl());
                    items.add(item);
                }
            }
        }

        return (ArrayList<VideoItem>) items;
    }


    @Override
    protected List<VideoItem> doInBackground(String... params) {
        try {
            return search(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<VideoItem> videoItems) {
        super.onPostExecute(videoItems);
       
        RecyclerView recyclerView = (RecyclerView) mWeakActivity.get().findViewById(R.id.currentPlaylistItems);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mWeakActivity.get());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter
        RecyclerView.Adapter mAdapter = new VideoAdapterRecycler(videoItems, new OnItemClickListenerImpl(mWeakActivity.get()), mWeakActivity.get());
        recyclerView.setAdapter(mAdapter);
        DrawerLayout drawer = (DrawerLayout) mWeakActivity.get().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        TextView title = (TextView) mWeakActivity.get().findViewById(R.id.currentPlaylistTitle);
        title.setText(R.string.search_results);
    }
}


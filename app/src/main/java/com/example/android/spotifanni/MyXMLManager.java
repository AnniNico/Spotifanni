package com.example.android.spotifanni;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by nicolo.mandrile on 24/05/2019.
 */

public class MyXMLManager  {

    ArrayList<Playlist> playlists;
    private Playlist playlist;
    private String text;

    public MyXMLManager() {
        playlists = new ArrayList<Playlist>();
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists){
        this.playlists = playlists;
    }

    public ArrayList<Playlist> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        Song song = new Song();
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Playlist")) {
                            // create a new instance of employee
                            playlist = new Playlist();
                            playlist.setSongs(new ArrayList<Song>());
                            playlists.add(playlist);
                        } else if (tagname.equalsIgnoreCase("Songs")) {
                            playlist.setSongs(new ArrayList<Song>());
                        } else if (tagname.equalsIgnoreCase("Song")) {
                            song = new Song();
                            playlist.getSongs().add(song);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(text!=null)
                        if (tagname.equalsIgnoreCase("PlaylistName")) {
                            playlist.setName(text);
                        } else if (tagname.equalsIgnoreCase("PlayListOrder")) {
                            playlist.setOrder(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("PlayListPlaying")) {
                            playlist.setPlaying(new Boolean(text));
                        }
                        else if (tagname.equalsIgnoreCase("Title")) {
                           if(song!=null) song.setTitle(text);
                        }
                        else if (tagname.equalsIgnoreCase("Author")) {
                            song.setAuthor(text);
                        }
                        else if (tagname.equalsIgnoreCase("Url")) {
                            song.setUrl(text);
                        }
                        else if (tagname.equalsIgnoreCase("Order")) {
                            song.setOrder(new Integer(text));
                        }
                        else if (tagname.equalsIgnoreCase("Playing")) {
                            song.setPlaying(new Boolean(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playlists;
    }



    public void writeXml(File file, ArrayList<Playlist> playlists){
        FileOutputStream fos;
        try {

            fos = new FileOutputStream(file);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "Playlists");
            for(Playlist p : playlists) {
                serializer.startTag(null, "Playlist");
                serializer.startTag(null, "PlaylistName");
                serializer.text("" + p.getName());
                serializer.endTag(null, "PlaylistName");
                serializer.startTag(null, "PlayListPlaying");
                serializer.text("" + p.isPlaying());
                serializer.endTag(null, "PlayListPlaying");
                serializer.startTag(null, "PlayListOrder");
                serializer.text("" + p.getOrder());
                serializer.endTag(null, "PlayListOrder");

                for (Song song : p.getSongs()) {
                    serializer.startTag(null, "Song");
                    serializer.startTag(null, "Title");
                    serializer.text(song.getTitle());
                    serializer.endTag(null, "Title");
                    serializer.startTag(null, "Author");
                    serializer.text(song.getAuthor());
                    serializer.endTag(null, "Author");
                    serializer.startTag(null, "Url");
                    serializer.text(song.getUrl());
                    serializer.endTag(null, "Url");
                    serializer.startTag(null, "Playing");
                    serializer.text("" + song.isPlaying());
                    serializer.endTag(null, "Playing");
                    serializer.startTag(null, "Order");
                    serializer.text("" + song.getOrder());
                    serializer.endTag(null, "Order");
                    serializer.startTag(null, "SongId");
                    serializer.text("" + song.getSongId());
                    serializer.endTag(null, "SongId");
                    serializer.endTag(null, "Song");
                }

                serializer.endTag(null, "Playlist");
            }
            serializer.endTag(null, "Playlists");


            serializer.endDocument();
            serializer.flush();

            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

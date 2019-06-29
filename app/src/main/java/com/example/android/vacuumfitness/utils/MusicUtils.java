package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    public static List<Song> getSongList(Context context){
        List<Song> songList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        String[] projection = {
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION

        };
        Cursor c = context.getContentResolver()
                .query(uri, projection, selection, null, sortOrder);

        if (c != null) {
            while (c.moveToNext()) {
                Song song = new Song();
                String path = c.getString(0);
                String artist = c.getString(1);
                String title = c.getString(2);
                String duration = c.getString(3);

                song.setPath(path);
                song.setSongArtist(artist);
                song.setSongName(title);
                song.setSongLength(duration);

                songList.add(song);
            }
            c.close();
        }

        return songList;
    }

    public static String getProperArtist(String artist){
        if(artist.equals("<unknown>")){
            return MainActivity.mContext.getResources().getString(R.string.various_artists);
        } else {
            return artist;
        }
    }
}

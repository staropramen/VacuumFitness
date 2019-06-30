package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

    //Returns a mediaSourcePlaylist to play in exoplayer
    public static ConcatenatingMediaSource getMediaSourcePlaylist(Context context, List<Song> songs){
        ConcatenatingMediaSource playlist = new ConcatenatingMediaSource();

        for (int i = 0; i < songs.size(); i++){
            Song currentSong = songs.get(i);
            Uri uri = Uri.parse(currentSong.getPath());
            String userAgent = Util.getUserAgent(context, KeyUtils.EXO_VIDEO_PLAYER);
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            playlist.addMediaSource(mediaSource);
        }

        return playlist;
    }

    public static String getProperArtist(String artist){
        if(artist.equals("<unknown>")){
            return MainActivity.mContext.getResources().getString(R.string.various_artists);
        } else {
            return artist;
        }
    }
}

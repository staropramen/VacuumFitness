package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
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

    public static String getPlaylistDuration(List<Song> songs){
        int durationInMillis = 0;

        for (int i = 0; i < songs.size(); i++){
            Song currentSong = songs.get(i);

            int songDuration = Integer.parseInt(currentSong.getSongLength());
            durationInMillis = durationInMillis + songDuration;
        }

        int durationInSeconds = durationInMillis / 1000;

        return PreparationUtils.secondsToTimeString(durationInSeconds);
    }

    public static Playlist getDeepBluePlaylist() {
        //App music 1
        List<Song> songs = new ArrayList<>();
        Song relaxSong = new Song("bensound_deepblue", "Bensound.com", "Deep Blue", "288000");
        songs.add(relaxSong);
        Playlist playlist = new Playlist("Deep Blue", songs, false);

        return playlist;
    }

    public static Playlist getSlowMotionPlaylist() {
        //App music 1
        List<Song> songs = new ArrayList<>();
        Song relaxSong = new Song("bensound_slowmotion", "Bensound.com", "Slow Motion", "288000");
        songs.add(relaxSong);
        Playlist playlist = new Playlist("Slow Motion", songs, false);

        return playlist;
    }

    public static Playlist getRelaxingPlaylist() {
        //App music 1
        List<Song> songs = new ArrayList<>();
        Song relaxSong = new Song("bensound_relaxing", "Bensound.com", "Relaxing", "206000");
        songs.add(relaxSong);
        Playlist playlist = new Playlist("Relaxing", songs, false);

        return playlist;
    }
}

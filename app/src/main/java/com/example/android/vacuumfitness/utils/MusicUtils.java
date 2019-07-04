package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    private static String RAW_SOURCE = "R.raw.";

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
            MediaSource mediaSource = makeMediaSource(context, currentSong.getPath());
            playlist.addMediaSource(mediaSource);
        }

        return playlist;
    }

    private static MediaSource makeMediaSource(Context context, String path){

        MediaSource mediaSource = new ConcatenatingMediaSource();

        //Get first 6 Characters of media path to check if it is a raw file
        String subString = path.substring(0, 6);

        if(subString != RAW_SOURCE){
            Uri uri = Uri.parse(path);
            String userAgent = Util.getUserAgent(context, KeyUtils.EXO_VIDEO_PLAYER);
            mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
        }else {
            final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.dummymusic));
            try {
                rawResourceDataSource.open(dataSpec);

                DataSource.Factory factory = new DataSource.Factory() {
                    @Override
                    public DataSource createDataSource() {
                        return rawResourceDataSource;
                    }
                };
                mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(rawResourceDataSource.getUri());

            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }
        }

        return mediaSource;
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
}

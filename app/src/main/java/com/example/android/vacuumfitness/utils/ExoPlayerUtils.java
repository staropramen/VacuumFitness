package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.EventListener;
import java.util.List;

public class ExoPlayerUtils {

    public static SimpleExoPlayer initializeExoPlayer(Context context, SimpleExoPlayer exoPlayer, Playlist playlist, Player.EventListener listenerContext){

        if(playlist.isCustom()){
            ConcatenatingMediaSource mediaSource = getMediaSourcePlaylist(context, playlist.getSongList());
            exoPlayer = prepareExoPlayerFromStorageResourceUri(context, exoPlayer, mediaSource);
        } else {
            List<Song> songs = playlist.getSongList();
            Song song = songs.get(0);
            Log.d("!!!!!!", song.getPath());
            exoPlayer = prepareExoPlayerFromRawResourceUri(context, exoPlayer, song.getPath());
        }

        exoPlayer.setPlayWhenReady(true);

        return exoPlayer;
    }

    //Initialize Exo Player Method
    private static SimpleExoPlayer prepareExoPlayerFromStorageResourceUri(Context context, SimpleExoPlayer exoPlayer, ConcatenatingMediaSource playlist){
        if(exoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            exoPlayer.prepare(playlist);
        }

        return exoPlayer;
    }

    private static SimpleExoPlayer prepareExoPlayerFromRawResourceUri(Context context, SimpleExoPlayer exoPlayer, String path){
        Uri uri = makeUriFromRaw(context, path);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());
        //exoPlayer.addListener(eventListener);

        DataSpec dataSpec = new DataSpec(uri);
        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
        try {
            rawResourceDataSource.open(dataSpec);
        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return rawResourceDataSource;
            }
        };

        MediaSource audioSource = new ExtractorMediaSource(rawResourceDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(audioSource);

        return exoPlayer;
    }

    private static Uri makeUriFromRaw(Context context, String rawString) {

        int rawId = context.getResources().getIdentifier(rawString, "raw", context.getPackageName());

        Uri uri = RawResourceDataSource.buildRawResourceUri(rawId);

       return uri;
    }

    //Returns a mediaSourcePlaylist to play in exoplayer
    private static ConcatenatingMediaSource getMediaSourcePlaylist(Context context, List<Song> songs){
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

    //Returns a mediaSourcePlaylist to play in exoplayer
    private static ConcatenatingMediaSource getRawSourcePlaylist(Context context, List<Song> songs){
        ConcatenatingMediaSource playlist = new ConcatenatingMediaSource();

        for (int i = 0; i < songs.size(); i++){
            Song currentSong = songs.get(i);
            Uri uri = makeUriFromRaw(context, currentSong.getPath());

            DataSpec dataSpec = new DataSpec(uri);
            final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
            try {
                rawResourceDataSource.open(dataSpec);
            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }

            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };

            MediaSource mediaSource = new ExtractorMediaSource(rawResourceDataSource.getUri(),
                    factory, new DefaultExtractorsFactory(), null, null);
            playlist.addMediaSource(mediaSource);
        }

        return playlist;
    }
}

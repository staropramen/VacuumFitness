package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.vacuumfitness.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

public class ExoPlayerUtils {

    public static void prepareExoPlayerFromRawResourceUri(Context context, SimpleExoPlayer exoPlayer, String path){
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
        exoPlayer.setPlayWhenReady(true);
    }

    public static Uri makeUriFromRaw(Context context, String rawString) {

        int rawId = context.getResources().getIdentifier(rawString, "raw", context.getPackageName());

        Uri uri = RawResourceDataSource.buildRawResourceUri(rawId);

       return uri;
    }
}

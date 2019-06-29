package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Song;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongAdapterViewHolder> {

    List<Song> songs;

    private final SongClickHandler songClickHandler;

    public interface SongClickHandler {
        void onClick(Song song);
    }

    public SongAdapter(SongClickHandler clickHandler){songClickHandler = clickHandler;}

    public class SongAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_artist) TextView artistName;
        @BindView(R.id.tv_song_title) TextView songTitle;
        @BindView(R.id.tv_duration) TextView songDuration;

        public SongAdapterViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Song song = songs.get(adapterPosition);
            songClickHandler.onClick(song);
        }
    }

    @NonNull
    @Override
    public SongAdapter.SongAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int listItem = R.layout.song_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

        return new SongAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongAdapterViewHolder viewHolder, int position) {
        Song song = songs.get(position);

        viewHolder.songTitle.setText(song.getSongName());
        viewHolder.artistName.setText(song.getSongArtist());
        viewHolder.songDuration.setText(song.getSongLength());
    }

    @Override
    public int getItemCount() {
        if (null == songs) return 0;
        return songs.size();
    }

    //Function to set moviesArray
    public void setSongs(List<Song> songList){
        songs = songList;
        notifyDataSetChanged();
    }
}

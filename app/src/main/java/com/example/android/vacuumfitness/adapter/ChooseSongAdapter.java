package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.example.android.vacuumfitness.utils.PreparationUtils;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseSongAdapter extends RecyclerView.Adapter<ChooseSongAdapter.ChooseSongAdapterViewHolder> {

    private List<Song> songs;
    private List<Song> chosenSongs;
    private Context mContext;

    private final ChooseSongClickHandler chooseSongClickHandler;

    public interface ChooseSongClickHandler {
        void onClick(Song song);
    }

    public ChooseSongAdapter(ChooseSongClickHandler clickHandler, List<Song> list, Context context){
        chooseSongClickHandler = clickHandler;
        chosenSongs = list;
        mContext = context;
    }

    public class ChooseSongAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_artist) TextView artistName;
        @BindView(R.id.tv_song_title) TextView songTitle;
        @BindView(R.id.tv_duration) TextView songDuration;
        @BindView(R.id.song_item) ConstraintLayout songItem;

        private ChooseSongAdapterViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Song song = songs.get(adapterPosition);

            //Set Background Color of item depending on if its in list or not
            if(chosenSongs != null && chosenSongs.contains(song)){
                songItem.setBackgroundColor(Color.TRANSPARENT);
            } else {
                songItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            }

            chooseSongClickHandler.onClick(song);
        }
    }

    @NonNull
    @Override
    public ChooseSongAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int listItem = R.layout.song_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

        return new ChooseSongAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseSongAdapterViewHolder viewHolder, int position) {
        Song song = songs.get(position);

        //Check if this item is already chosen
        if(chosenSongs != null && chosenSongs.contains(song)){
            viewHolder.songItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            viewHolder.songItem.setBackgroundColor(Color.TRANSPARENT);
        }

        viewHolder.songTitle.setText(song.getSongName());
        String artist = MusicUtils.getProperArtist(song.getSongArtist());
        viewHolder.artistName.setText(artist);
        int timeInSeconds = Integer.parseInt(song.getSongLength()) / 1000;
        viewHolder.songDuration.setText(PreparationUtils.secondsToTimeString(timeInSeconds));
    }

    @Override
    public int getItemCount() {
        if (null == songs) return 0;
        return songs.size();
    }

    public void setSongs(List<Song> songList){
        songs = songList;
        notifyDataSetChanged();
    }
}

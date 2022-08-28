package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistAdapterViewHolder> {

    private List<Playlist> playlists;

    private final PlaylistAdapterOnClickHandler playlistAdapterOnClickHandler;

    private final PlaylistAdapterOnLongClickHandler playlistAdapterOnLongClickHandler;

    public interface PlaylistAdapterOnClickHandler {
        void onClick(Playlist playlist, boolean equals);
    }

    public interface PlaylistAdapterOnLongClickHandler {
        void onLongClick(Playlist playlist);
    }

    //Constructor
    public PlaylistAdapter(PlaylistAdapterOnClickHandler clickHandler, PlaylistAdapterOnLongClickHandler longClickHandler){
        playlistAdapterOnClickHandler = clickHandler;
        playlistAdapterOnLongClickHandler = longClickHandler;
    }

    //ViewHolder
    public class PlaylistAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.tv_playlist_name) TextView nameTextView;
        @BindView(R.id.iv_more_options) ImageView moreOptions;

        public PlaylistAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            moreOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean equals = view.equals(this.moreOptions);
            int adapterPosition = getAdapterPosition();
            Playlist playlist = playlists.get(adapterPosition);
            playlistAdapterOnClickHandler.onClick(playlist, equals);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            Playlist playlist = playlists.get(adapterPosition);
            playlistAdapterOnLongClickHandler.onLongClick(playlist);
            return true;
        }
    }

    @NonNull
    @Override
    public PlaylistAdapter.PlaylistAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int listItem = R.layout.playlist_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

        return new PlaylistAdapter.PlaylistAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistAdapterViewHolder viewHolder, int position) {
        Playlist playlist = playlists.get(position);
        viewHolder.nameTextView.setText(playlist.getPlaylistName());
    }

    @Override
    public int getItemCount() {
        if (null == playlists) return 0;
        return playlists.size();
    }

    //Function to set PlaylistList
    public void setPlaylists(List<Playlist> playlistList){
        playlists = playlistList;
        notifyDataSetChanged();
    }
}

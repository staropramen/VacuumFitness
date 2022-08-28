package com.example.android.vacuumfitness.ui;


import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.PlaylistAdapter;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.viewmodel.PlaylistViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomizeMusicFragment extends Fragment implements PlaylistAdapter.PlaylistAdapterOnClickHandler, PlaylistAdapter.PlaylistAdapterOnLongClickHandler {

    private LinearLayoutManager layoutManager;
    private PlaylistAdapter playlistAdapter;
    @BindView(R.id.rv_playlist) RecyclerView playlistRecyclerView;
    @BindView(R.id.tv_empty_playlist_list) TextView emptyListTextView;
    @BindView(R.id.bt_add_playlist) Button mAddPlaylistButton;

    private Playlist mPlaylistToEdit;

    public CustomizeMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customize_music, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.customize_music_title);

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        playlistRecyclerView.setLayoutManager(layoutManager);
        playlistAdapter = new PlaylistAdapter(this, this);
        playlistRecyclerView.setAdapter(playlistAdapter);

        setupViewModel();

        setupFabButton();

        return rootView;
    }

    @Override
    public void onClick(Playlist playlist, boolean equals) {
        if (equals) {
            mPlaylistToEdit = playlist;
            showAlertDialogButtonClicked(true);
        } else {
            long id = playlist.getPrimaryKey();
            playlistDetailFragmentTransaction(id);
        }

    }

    @Override
    public void onLongClick(Playlist playlist) {
        mPlaylistToEdit = playlist;
        showAlertDialogButtonClicked(true);
    }

    private void setupViewModel(){
        PlaylistViewModel viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        viewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                if(playlists.isEmpty()){
                    //Show empty view
                    emptyListTextView.setVisibility(View.VISIBLE);
                    playlistRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyListTextView.setVisibility(View.GONE);
                    playlistRecyclerView.setVisibility(View.VISIBLE);
                    playlistAdapter.setPlaylists(playlists);
                }
            }
        });
    }

    private void setupFabButton() {
        mAddPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(false);
            }
        });
    }

    private Playlist makeNewPlaylist(String name){
        List<Song> list = new ArrayList<>();
        Playlist playlist = new Playlist(name, list);
        return playlist;
    }

    private void saveNewPlaylistInDb(final Playlist playlist) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Long id = AppDatabase.getInstance(getActivity()).playlistDao().insertPlaylist(playlist);

                playlistDetailFragmentTransaction(id);
            }
        });
    }

    private void updateNewPlaylistInDb(final Playlist playlist) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getActivity()).playlistDao().updatePlaylist(playlist);

                playlistDetailFragmentTransaction(playlist.getPrimaryKey());
            }
        });
    }

    private void playlistDetailFragmentTransaction(long id) {
        Bundle data = new Bundle();
        data.putLong(KeyUtils.PLAYLIST_ID_KEY, id);
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customize_music_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showAlertDialogButtonClicked(final boolean isLongClick) {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.custom_playlist_dialog_title));

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.create_playlist_dialog, null);
        builder.setView(customLayout);

        final EditText nameET = customLayout.findViewById(R.id.et_custom_playlist_name);

        //If edit mode do different
        if(isLongClick){
            //Set delete icon as visible
            customLayout.findViewById(R.id.iv_delete_icon).setVisibility(View.VISIBLE);
            //Set the name
            nameET.setText(mPlaylistToEdit.getPlaylistName());
        }

        // add a button
        builder.setPositiveButton(getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = nameET.getText().toString();

                //Cancel if User dont entern a Training Name
                if(name.matches("")){
                    Toast.makeText(getActivity(), getString(R.string.no_playlist_name), Toast.LENGTH_LONG).show();
                } else if(isLongClick){
                    mPlaylistToEdit.setPlaylistName(name);
                    updateNewPlaylistInDb(mPlaylistToEdit);
                }else {
                    Playlist playlist = makeNewPlaylist(name);
                    saveNewPlaylistInDb(playlist);
                }
            }
        });

        //Negative button
        builder.setNegativeButton(getString(R.string.negative_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // create the alert dialog
        final AlertDialog dialog = builder.create();

        //Set delete on click function
        customLayout.findViewById(R.id.iv_delete_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(mPlaylistToEdit);
                dialog.cancel();
            }
        });

        //Show Dialog
        dialog.show();
    }

    private void showDeleteDialog(final Playlist playlist){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        alert.setTitle(getString(R.string.delete_playlist_title));
        alert.setMessage(getString(R.string.delete_question, playlist.getPlaylistName()));
        alert.setPositiveButton(getString(R.string.delete_answer), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(getActivity()).playlistDao().deletePlaylist(playlist);
                    }
                });
            }
        });
        alert.setNegativeButton(getString(R.string.negative_answer), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();
    }
}

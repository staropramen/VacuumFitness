package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Training;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomTrainingAdapter extends RecyclerView.Adapter<CustomTrainingAdapter.CustomTrainingAdapterViewHolder> {

    private List<Training> trainings;

    private final CustomTrainingOnClickHandler customTrainingOnClickHandler;

    private final CustomTrainingOnLongClickHandler customTrainingOnLongClickHandler;

    public interface CustomTrainingOnClickHandler {
        void onClick(Training training);
    }

    public interface CustomTrainingOnLongClickHandler {
        void onLongClick(Training training);
    }

    //Constructor
    public CustomTrainingAdapter(CustomTrainingOnClickHandler clickHandler, CustomTrainingOnLongClickHandler longClickHandler){
        customTrainingOnClickHandler = clickHandler;
        customTrainingOnLongClickHandler = longClickHandler;
    }

    //ViewHolder
    public class CustomTrainingAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.tv_label) TextView labelTextView;
        @BindView(R.id.tv_custom_training_name) TextView nameTextView;

        public CustomTrainingAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Training training = trainings.get(adapterPosition);
            customTrainingOnClickHandler.onClick(training);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            Training training = trainings.get(adapterPosition);
            customTrainingOnLongClickHandler.onLongClick(training);
            return true;
        }
    }

    @NonNull
    @Override
    public CustomTrainingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int listItem = R.layout.custom_training_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

        return new CustomTrainingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomTrainingAdapterViewHolder viewHolder, int position) {
        Training training = trainings.get(position);
        viewHolder.labelTextView.setText(training.getLabel());
        viewHolder.nameTextView.setText(training.getTrainingName());
    }

    @Override
    public int getItemCount() {
        if (null == trainings) return 0;
        return trainings.size();
    }

    //Function to set Trainings List
    public void setTrainings(List<Training> trainingsToSet){
        trainings = trainingsToSet;
        notifyDataSetChanged();
    }
}

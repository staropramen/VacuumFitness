package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.utils.TrainingTimerUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterViewHolder> {

    private List<Exercise> exercises;

    private final ExerciseAdapterClickHandler exerciseOnClickHandler;

    public interface ExerciseAdapterClickHandler {
        void onClick(Exercise exercise);
    }

    //Constructor
    public ExerciseAdapter(ExerciseAdapterClickHandler clickHandler){exerciseOnClickHandler = clickHandler;}

    //ViewHolder
    public class ExerciseAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_exercise_thumbnail) ImageView exerciseThumbnail;
        @BindView(R.id.tv_exercise_list_name) TextView nameTextView;

        public ExerciseAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Exercise exercise = exercises.get(adapterPosition);
            exerciseOnClickHandler.onClick(exercise);
        }
    }

    @NonNull
    @Override
    public ExerciseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int listItem = R.layout.exercise_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

        return new ExerciseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapterViewHolder viewHolder, int position) {
        Exercise exercise = exercises.get(position);
        viewHolder.nameTextView.setText(exercise.getExerciseName());
        //Set exercise image
        String imagePath = exercise.getImage();
        int resId = TrainingTimerUtils.getResId(imagePath, R.drawable.class);
        if (resId != -1) {
            Picasso.get().load(resId).into(viewHolder.exerciseThumbnail);
        } else {
            //If path not found load a dummy picture
            Picasso.get().load(R.drawable.dummy1).into(viewHolder.exerciseThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (null == exercises) return 0;
        return exercises.size();
    }

    //Function to set moviesArray
    public void setExercises(List<Exercise> exerciseList){
        exercises = exerciseList;
        notifyDataSetChanged();
    }
}

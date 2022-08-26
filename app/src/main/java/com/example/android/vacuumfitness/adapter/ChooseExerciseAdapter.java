package com.example.android.vacuumfitness.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
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

public class ChooseExerciseAdapter extends RecyclerView.Adapter<ChooseExerciseAdapter.ChooseExerciseAdapterViewHolder> {

    private List<Exercise> exercises;
    private List<Exercise> chosenExercises;
    private Context mContext;

    private final ChooseExerciseClickHandler exerciseOnClickHandler;

    public interface ChooseExerciseClickHandler {
        void onClick(Exercise exercise);
    }

    //Constructor
    public ChooseExerciseAdapter(ChooseExerciseClickHandler clickHandler, List<Exercise> list, Context context){
        exerciseOnClickHandler = clickHandler;
        chosenExercises = list;
        mContext = context;
    }

    //ViewHolder
    public class ChooseExerciseAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_exercise_thumbnail)
        ImageView exerciseThumbnail;
        @BindView(R.id.tv_exercise_list_name)
        TextView nameTextView;
        @BindView(R.id.exercise_item)
        ConstraintLayout exerciseItem;

        public ChooseExerciseAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Exercise exercise = exercises.get(adapterPosition);

            //Set Backgroundcolor of item depending on if its in list or not
            if(chosenExercises != null && chosenExercises.contains(exercise)){
                exerciseItem.setBackgroundColor(Color.TRANSPARENT);
            } else {
                exerciseItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            }

            exerciseOnClickHandler.onClick(exercise);
        }
    }

        @NonNull
        @Override
        public ChooseExerciseAdapter.ChooseExerciseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            int listItem = R.layout.exercise_list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(listItem, viewGroup,shouldAttachToParentImmediately);

            return new ChooseExerciseAdapter.ChooseExerciseAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChooseExerciseAdapter.ChooseExerciseAdapterViewHolder viewHolder, int position) {
            Exercise exercise = exercises.get(position);

            //Check if this item is already chosen
            if(chosenExercises != null && chosenExercises.contains(exercise)){
                viewHolder.exerciseItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            } else {
                viewHolder.exerciseItem.setBackgroundColor(Color.TRANSPARENT);
            }

            viewHolder.nameTextView.setText(exercise.getExerciseName());
            //Set exercise image
            String imagePath = exercise.getImage();
            int resId = TrainingTimerUtils.getResId(imagePath, R.drawable.class);
            if (resId != -1) {
                Picasso.get().load(resId).into(viewHolder.exerciseThumbnail);
            } else {
                //If path not found load a dummy picture
                Picasso.get().load(R.drawable.eagle).into(viewHolder.exerciseThumbnail);
            }
        }

        @Override
        public int getItemCount() {
            if (null == exercises) return 0;
            return exercises.size();
        }

        public void setExercises(List<Exercise> exerciseList){
            exercises = exerciseList;
            notifyDataSetChanged();
        }
}

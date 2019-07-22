package com.example.android.vacuumfitness.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, Training.class, Playlist.class, Motivator.class}, version = 1, exportSchema = false)
@TypeConverters({ListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "vacuum-fitness";
    private static AppDatabase sInstance;
    private static Context mainContext = MainActivity.mContext;

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Insert the Exercises
                                        getInstance(context).exerciseDao().insertAll(populateExercises());
                                        List<Integer> exerciseIds = getInstance(context).exerciseDao().loadExerciseIdArray();
                                        SharedPrefsUtils.saveExerciseIdsToSharedPrefs(context, exerciseIds);

                                        //Insert Motivators
                                        getInstance(context).motivatorDao().insertAll(populateMotivators());

                                        Log.d(LOG_TAG, "OnCreate DB called");
                                    }
                                });
                            }
                        })
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ExerciseDao exerciseDao();

    public abstract TrainingDao trainingDao();

    public abstract PlaylistDao playlistDao();

    public abstract MotivatorDao motivatorDao();

    private static Exercise[] populateExercises(){
        return new Exercise[] {
                new Exercise(mainContext.getString(R.string.silent_tree), "silent_tree", ""),
                new Exercise(mainContext.getString(R.string.eagle), "eagle", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.mantis), "mantis", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.armadillo), "armadillo", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.prayer), "prayer", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.tamarin), "tamarin", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.orchid), "orchid", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.bear), "bear", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.sun_king), "sun_king", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.peacock), "peacock", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.tiger), "tiger", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.cobra), "cobra", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.hedgehog), "hedgehog", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.polar_fox), "polar_fox", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.kangaroo), "kangaroo", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.rosebush), "rosebush", "xMQwBMhaqFA"),
                new Exercise(mainContext.getString(R.string.nightshade), "nightshade", "xMQwBMhaqFA"),
        };
    }

    private static Motivator[] populateMotivators() {
        return new Motivator[] {
                new Motivator(1, "Always keep on track!"),
                new Motivator(2, "Never give up!"),
                new Motivator(3, "You are one of the best!"),
                new Motivator(4, "Vacuum Fitness believes in you"),
                new Motivator(5, "You are sexy as you are!"),
        };
    }
}

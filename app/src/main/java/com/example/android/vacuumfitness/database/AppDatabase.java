package com.example.android.vacuumfitness.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import java.util.List;

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
                new Exercise(mainContext.getString(R.string.silent_tree), "silent_tree", "MvbMZr1IMNM"),
                new Exercise(mainContext.getString(R.string.eagle), "eagle", "KwxPCGvn3AU"),
                new Exercise(mainContext.getString(R.string.mantis), "mantis", "N7dl8P1D8kI"),
                new Exercise(mainContext.getString(R.string.armadillo), "armadillo", "7-Q4_TrRWEU"),
                new Exercise(mainContext.getString(R.string.prayer), "prayer", "E4DBzlNRBj8"),
                new Exercise(mainContext.getString(R.string.tamarin), "tamarin", "HIBZUj_tsU4"),
                new Exercise(mainContext.getString(R.string.orchid), "orchid", "8eXIB4n-XII"),
                new Exercise(mainContext.getString(R.string.bear), "bear", "IjGnTQAVw_0"),
                new Exercise(mainContext.getString(R.string.sun_king), "sun_king", "Eqz1em17AVg"),
                new Exercise(mainContext.getString(R.string.peacock), "peacock", "jHLrP8AXR9M"),
                new Exercise(mainContext.getString(R.string.tiger), "tiger", "b05V2NC3qpo"),
                new Exercise(mainContext.getString(R.string.cobra), "cobra", "b9LbPAJrbHs"),
                new Exercise(mainContext.getString(R.string.hedgehog), "hedgehog", "h_MRemF-HeU"),
                new Exercise(mainContext.getString(R.string.polar_fox), "polar_fox", "M3fhEMDIr0s"),
                new Exercise(mainContext.getString(R.string.kangaroo), "kangaroo", "W1YlnKD1Ep8"),
                new Exercise(mainContext.getString(R.string.rosebush), "rosebush", "Gyj50Ksa7yA"),
                new Exercise(mainContext.getString(R.string.nightshade), "nightshade", "G-jnDlJMSX8"),
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

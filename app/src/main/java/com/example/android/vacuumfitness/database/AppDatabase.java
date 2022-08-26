package com.example.android.vacuumfitness.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import androidx.annotation.NonNull;
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
                new Exercise(mainContext.getString(R.string.silent_tree), "silent_tree", mainContext.getString(R.string.silent_tree_key)),
                new Exercise(mainContext.getString(R.string.eagle), "eagle", mainContext.getString(R.string.eagle_key)),
                new Exercise(mainContext.getString(R.string.mantis), "mantis", mainContext.getString(R.string.mantis_key)),
                new Exercise(mainContext.getString(R.string.armadillo), "armadillo", mainContext.getString(R.string.armadillo_key)),
                new Exercise(mainContext.getString(R.string.prayer), "prayer", mainContext.getString(R.string.prayer_key)),
                new Exercise(mainContext.getString(R.string.tamarin), "tamarin", mainContext.getString(R.string.tamarin_key)),
                new Exercise(mainContext.getString(R.string.orchid), "orchid", mainContext.getString(R.string.orchid_key)),
                new Exercise(mainContext.getString(R.string.bear), "bear", mainContext.getString(R.string.bear_key)),
                new Exercise(mainContext.getString(R.string.sun_king), "sun_king", mainContext.getString(R.string.sun_king_key)),
                new Exercise(mainContext.getString(R.string.peacock), "peacock", mainContext.getString(R.string.peacock_key)),
                new Exercise(mainContext.getString(R.string.tiger), "tiger", mainContext.getString(R.string.tiger_key)),
                new Exercise(mainContext.getString(R.string.cobra), "cobra", mainContext.getString(R.string.cobra_key)),
                new Exercise(mainContext.getString(R.string.hedgehog), "hedgehog", mainContext.getString(R.string.hedgehog_key)),
                new Exercise(mainContext.getString(R.string.polar_fox), "polar_fox", mainContext.getString(R.string.polar_fox_key)),
                new Exercise(mainContext.getString(R.string.kangaroo), "kangaroo", mainContext.getString(R.string.kangaroo_key)),
                new Exercise(mainContext.getString(R.string.rosebush), "rosebush", mainContext.getString(R.string.rosebush_key)),
                new Exercise(mainContext.getString(R.string.nightshade), "nightshade", mainContext.getString(R.string.nightshade_key)),
        };
    }

    private static Motivator[] populateMotivators() {
        return new Motivator[] {
                new Motivator(1, mainContext.getString(R.string.motivator_1)),
                new Motivator(2, mainContext.getString(R.string.motivator_1)),
                new Motivator(3, mainContext.getString(R.string.motivator_3)),
                new Motivator(4, mainContext.getString(R.string.motivator_4)),
                new Motivator(5, mainContext.getString(R.string.motivator_5)),
        };
    }
}

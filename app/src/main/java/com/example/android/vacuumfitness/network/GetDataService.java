package com.example.android.vacuumfitness.network;

import android.content.Context;

import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.ui.MainActivity;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("{path}")
    Call<List<Motivator>> getAllMotivators(@Path("path") String path);
}

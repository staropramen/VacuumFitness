package com.example.android.vacuumfitness.network;

import com.example.android.vacuumfitness.model.Motivator;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("motivators.json")
    Call<List<Motivator>> getAllMotivators();
}

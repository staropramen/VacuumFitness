package com.example.android.vacuumfitness.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    //Base URL for API request
    private static final String EXERCISE_DATA_URL =
            "https://staropramen.github.io/gym/";

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(EXERCISE_DATA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GetDataService getApiService() {
        return getRetrofitInstance().create(GetDataService.class);
    }
}

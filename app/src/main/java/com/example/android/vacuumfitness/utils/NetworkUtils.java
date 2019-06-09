package com.example.android.vacuumfitness.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.vacuumfitness.ui.MainActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {

    private static ConnectivityManager mConnectivityManager;

    public static boolean isConnectedToInternet() {
        //Check if there is an internet connection
        mConnectivityManager = (ConnectivityManager) MainActivity.mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();

        //Load Movie Data if device is connected to internet, else show error message
        if(activeNetwork != null && activeNetwork.isConnected()){
            return true;
        }else {
            return false;
        }
    }

}

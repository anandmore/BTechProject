package com.btech.project.technofeed.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.btech.project.technofeed.TechnoFeedApplication;

public class UtilityMethods {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) TechnoFeedApplication.getTechnoFeedApplicationInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
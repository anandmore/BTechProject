package com.btech.project.technofeed;

import android.app.Application;

public class TechnoFeedApplication extends Application {
    private static TechnoFeedApplication technoFeedApplicationInstance;

    public static TechnoFeedApplication getTechnoFeedApplicationInstance() {
        return technoFeedApplicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        technoFeedApplicationInstance = this;
    }
}

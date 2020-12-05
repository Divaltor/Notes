package com.divaltor.notes;

import android.app.Application;

public class NotesApp extends Application {
    public static NotesApp APP_INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        APP_INSTANCE = this;
    }
}

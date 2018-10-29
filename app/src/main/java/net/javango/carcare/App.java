package net.javango.carcare;

import android.app.Application;
import android.content.Context;

import net.javango.carcare.model.AppDatabase;

public class App extends Application {

    private static Context context;
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}

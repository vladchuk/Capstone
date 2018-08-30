package net.javango.carcare.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

public class CarListModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = CarListModel.class.getSimpleName();

    private LiveData<List<Car>> tasks;

    // must be at least protected
    protected CarListModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.carDao().getAll();
    }

    public LiveData<List<Car>> getCars() {
        return tasks;
    }

    public static CarListModel getInstance(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(CarListModel.class);
    }

}

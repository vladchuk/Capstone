package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

public class CarModel extends ViewModel {

    private LiveData<Car> car;

    // must be at least protected
    protected CarModel(AppDatabase database, int carId) {
        car = database.carDao().findById(carId);
    }

    public LiveData<Car> getCar() {
        return car;
    }

    /**
     * Factory method in obtain an instance of this model by carId
     */
    public static CarModel getInstance(Fragment fragment, AppDatabase db, int carId) {
        fragment.getActivity().getApplication();
        CarModelFactory factory = new CarModelFactory(db, carId);
        CarModel viewModel = ViewModelProviders.of(fragment, factory).get(CarModel.class);
        return viewModel;
    }

    private static class CarModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final AppDatabase db;
        private final int carId;

        public CarModelFactory(AppDatabase database, int taskId) {
            db = database;
            carId = taskId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CarModel(db, carId);
        }
    }

}

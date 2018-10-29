package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import net.javango.carcare.App;

public class CarDetailModel extends ViewModel {

    private LiveData<Car> car;

    // must be at least protected
    protected CarDetailModel(int carId) {
        car = AppDatabase.getDatabase().carDao().findById(carId);
    }

    public LiveData<Car> getCar() {
        return car;
    }

    /**
     * Factory method to obtain an instance of this model by carId
     */
    public static CarDetailModel getInstance(Fragment fragment, AppDatabase db, int carId) {
        CarModelFactory factory = new CarModelFactory(carId);
        CarDetailModel viewModel = ViewModelProviders.of(fragment, factory).get(CarDetailModel.class);
        return viewModel;
    }

    private static class CarModelFactory implements ViewModelProvider.Factory {

        private final int carId;

        public CarModelFactory(int cid) {
            carId = cid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CarDetailModel(carId);
        }
    }

}

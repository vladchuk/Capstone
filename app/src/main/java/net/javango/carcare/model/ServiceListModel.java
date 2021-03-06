package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import java.util.List;

public class ServiceListModel extends ViewModel {

    private LiveData<List<Service>> services;

    // must be at least protected
    protected ServiceListModel(int carId) {
        services = AppDatabase.getDatabase().serviceDao().getForCar(carId);
    }

    public LiveData<List<Service>> getServices() {
        return services;
    }

    /**
     * Factory method to obtain an instance of this model by carId
     */
    public static ServiceListModel getInstance(Fragment fragment, int carId) {
        ServiceModelFactory factory = new ServiceModelFactory(carId);
        ServiceListModel viewModel = ViewModelProviders.of(fragment, factory).get(ServiceListModel.class);
        return viewModel;
    }

    // required because services are looked up by carId
    private static class ServiceModelFactory implements ViewModelProvider.Factory {

        private final int carId;

        public ServiceModelFactory(int cid) {
            carId = cid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ServiceListModel(carId);
        }
    }

}

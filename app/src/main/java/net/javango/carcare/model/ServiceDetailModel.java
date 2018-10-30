package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

public class ServiceDetailModel extends ViewModel {

    private LiveData<Service> service;

    // must be at least protected
    protected ServiceDetailModel(int serviceId) {
        service = AppDatabase.getDatabase().serviceDao().findById(serviceId);
    }

    public LiveData<Service> getService() {
        return service;
    }

    /**
     * Factory method to obtain an instance of this model by carId
     */
    public static ServiceDetailModel getInstance(Fragment fragment, int carId) {
        ServiceModelFactory factory = new ServiceModelFactory(carId);
        ServiceDetailModel viewModel = ViewModelProviders.of(fragment, factory).get(ServiceDetailModel.class);
        return viewModel;
    }

    private static class ServiceModelFactory implements ViewModelProvider.Factory {

        private final int carId;

        public ServiceModelFactory(int cid) {
            carId = cid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ServiceDetailModel(carId);
        }
    }

}

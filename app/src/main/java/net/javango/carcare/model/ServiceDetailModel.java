package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

public class ServiceDetailModel extends ViewModel {

    private LiveData<Service> service;

    // must be at least protected
    protected ServiceDetailModel(AppDatabase database, int serviceId) {
        service = database.serviceDao().findById(serviceId);
    }

    public LiveData<Service> getService() {
        return service;
    }

    /**
     * Factory method in obtain an instance of this model by carId
     */
    public static ServiceDetailModel getInstance(Fragment fragment, AppDatabase db, int carId) {
        fragment.getActivity().getApplication();
        ServiceModelFactory factory = new ServiceModelFactory(db, carId);
        ServiceDetailModel viewModel = ViewModelProviders.of(fragment, factory).get(ServiceDetailModel.class);
        return viewModel;
    }

    private static class ServiceModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final AppDatabase db;
        private final int carId;

        public ServiceModelFactory(AppDatabase database, int cid) {
            db = database;
            carId = cid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ServiceDetailModel(db, carId);
        }
    }

}

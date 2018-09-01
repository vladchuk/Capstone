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
    protected ServiceListModel(AppDatabase database, int carId) {
        services = database.serviceDao().getForCar(carId);
    }

    public LiveData<List<Service>> getServices() {
        return services;
    }

    /**
     * Factory method in obtain an instance of this model by carId
     */
    public static ServiceListModel getInstance(Fragment fragment, AppDatabase db, int carId) {
        fragment.getActivity().getApplication();
        ServiceModelFactory factory = new ServiceModelFactory(db, carId);
        ServiceListModel viewModel = ViewModelProviders.of(fragment, factory).get(ServiceListModel.class);
        return viewModel;
    }

    // required because services are looked up by carId
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
            return (T) new ServiceListModel(db, carId);
        }
    }

}

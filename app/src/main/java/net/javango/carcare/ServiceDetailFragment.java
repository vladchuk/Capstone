package net.javango.carcare;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.javango.carcare.databinding.FragmentServiceDetailBinding;
import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceDetailModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;

import java.util.Date;

/**
 * Handles {@code Service} UI. Used to insert, update or delete services.
 */
public class ServiceDetailFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";
    private static final String ARG_SERVICE_ID = "service_id";

    private AppDatabase database;
    private Integer serviceId;
    private int carId; // should never be null

    FragmentServiceDetailBinding binding;

    /**
     * Creates an instance if this fragment.
     *
     * @param serviceId id of the {@code Service} to display and process in this fragment. If {@code null}, it means
     *                  a new service is being added
     */
    public static ServiceDetailFragment newInstance(int carId, Integer serviceId) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CAR_ID, carId);
        bundle.putSerializable(ARG_SERVICE_ID, serviceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(getActivity());
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.service_details);
        carId = getArguments().getInt(ARG_CAR_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_service_detail, container, false);
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_service_detail, container, false);

        binding.serviceCancelButton.setOnClickListener(view -> getActivity().finish());
        binding.serviceSaveButton.setOnClickListener(view -> onSaveButtonClicked());

        if (savedInstanceState == null) {
            serviceId = (Integer) getArguments().getSerializable(ARG_SERVICE_ID);
            if (serviceId != null) {
                final ServiceDetailModel viewModel = ServiceDetailModel.getInstance(this, database, serviceId);
                viewModel.getService().observe(this, new Observer<Service>() {
                    @Override
                    public void onChanged(@Nullable Service service) {
                        viewModel.getService().removeObserver(this);
                        populateUI(service);
                    }
                });
            }
        } else {
            serviceId = (Integer) savedInstanceState.getSerializable(ARG_SERVICE_ID);
        }
        binding.serviceSaveButton.setText(serviceId == null ? R.string.add_button : R.string.update_button);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_SERVICE_ID, serviceId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(Service service) {
        binding.serviceDetailDescription.setText(service.getDescription());
        binding.serviceDetailDateValue.setText(Formatter.format(service.getDate()));
        binding.serviceDetailMileageValue.setText(Formatter.format(service.getMileage()));
        binding.serviceDetailCostValue.setText(Formatter.format(service.getCost()));
        binding.serviceDetailLocationValue.setText(service.getLocation());
        binding.serviceDetailNotesValue.setText(service.getNotes());
    }

    private void populateService(Service service) {
        service.setDescription((binding.serviceDetailDescription.getText().toString()));
        Date date = Formatter.parseDate(binding.serviceDetailDateValue.getText().toString());
        service.setDate(date);
        Integer mileage = Formatter.parseInt(binding.serviceDetailMileageValue.getText().toString());
        service.setMileage(mileage);
        Integer cost = Formatter.parseInt(binding.serviceDetailCostValue.getText().toString());
        service.setCost(cost);
        service.setLocation(binding.serviceDetailLocationValue.getText().toString());
        service.setNotes(binding.serviceDetailNotesValue.getText().toString());
    }

    /**
     * Inserts that new service data into the underlying database.
     */
    public void onSaveButtonClicked() {
        Service service = new Service(carId);
        populateService(service);

        TaskExecutor.executeDisk(new Runnable() {
            @Override
            public void run() {
                if (serviceId == null) {
                    // insert
                    database.serviceDao().addService(service);
                } else {
                    //update
                    service.setId(serviceId);
                    database.serviceDao().updateService(service);
                }
                getActivity().finish();
            }
        });
    }
}

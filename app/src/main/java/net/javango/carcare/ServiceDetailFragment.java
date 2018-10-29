package net.javango.carcare;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.javango.carcare.databinding.FragmentServiceDetailBinding;
import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarDetailModel;
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceDetailModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;
import net.javango.common.comp.DatePickerFragment;

import java.util.Date;

/**
 * Handles {@code Service} UI. Used to insert, update or delete services.
 */
public class ServiceDetailFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";
    private static final String ARG_SERVICE_ID = "service_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

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
        database = AppDatabase.getDatabase();
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.service_details);
        carId = getArguments().getInt(ARG_CAR_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_service_detail, container, false);

        binding.serviceDetailDateValue.setOnClickListener(view -> showDatePicker());

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
        CarDetailModel.getInstance(this, database, carId).getCar().observe(this, (car) -> setTitle(car));
        return binding.getRoot();
    }

    private void showDatePicker() {
        FragmentManager manager = getFragmentManager();
        String title = getString(R.string.date_of_service);
        Date date = Formatter.parseDate(binding.serviceDetailDateValue.getText().toString());
        if (date == null)
            date = new Date();
        DatePickerFragment dialog = DatePickerFragment.newInstance(null, date);
        dialog.setTargetFragment(ServiceDetailFragment.this, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    private void setTitle(Car car) {
        String title = car.getName();
        getActivity().setTitle(title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_SERVICE_ID, serviceId);
        super.onSaveInstanceState(outState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_service_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.service_save:
                saveService();
                break;
            case R.id.service_delete:
                if (serviceId != null)
                    TaskExecutor.executeDisk(() -> AppDatabase.getDatabase().serviceDao().deleteById(serviceId));
                break;
        }
        getActivity().finish();
        return true;
    }

    private void populateUI(Service service) {
        binding.serviceDetailDescription.setText(service.getDescription());
        binding.serviceDetailDateValue.setText(Formatter.formatFull(service.getDate()));
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
    public void saveService() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            binding.serviceDetailDateValue.setText(Formatter.formatFull(date));
        }
    }
}

package net.javango.carcare;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceDetailModel;
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceDetailModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;

/**
 * Handles {@code Service} UI. Used to insert, update or delete services.
 */
public class ServiceDetailFragment extends Fragment {

    private static final String ARG_SERVICE_ID = "service_id";

    private EditText name;
    private EditText year;
    private EditText tire;
    private Button cancel;
    private Button save;
    private AppDatabase database;
    private Integer serviceId;

    /**
     * Creates an instance if this fragment.
     *
     * @param serviceId id of the {@code Service} to display and process in this fragment. If {@code null}, it means a new
     *              service is being added
     */
    public static ServiceDetailFragment newInstance(Integer serviceId) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle bundle = new Bundle();
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


        if (savedInstanceState == null) {
            serviceId = (Integer) getArguments().getSerializable(ARG_SERVICE_ID);
            if (serviceId != null) {
//            mButton.setText(R.string.update_button);
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_detail, container, false);

//        name = v.findViewById(R.id.service_name_value);
//        year = v.findViewById(R.id.service_year_value);
//        tire = v.findViewById(R.id.service_tire_value);
//        cancel = v.findViewById(R.id.service_cancel_button);
//        save = v.findViewById(R.id.service_save_button);
//        cancel.setOnClickListener(view -> getActivity().finish());
//        save.setOnClickListener(view -> onSaveButtonClicked());

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_SERVICE_ID, serviceId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(Service service) {
//        name.setText(service.getName());
//        year.setText(Formatter.toString(service.getModelYear()));
//        tire.setText(service.getTireSize());
    }

    /**
     * Inserts that new service data into the underlying database.
     */
    public void onSaveButtonClicked() {
        final Service service = new Service();
//        service.setName(name.getText().toString());
//        Integer y = Formatter.parseInt(year.getText().toString());
//        service.setModelYear(y);
//        service.setTireSize(tire.getText().toString());

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

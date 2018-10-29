package net.javango.carcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarDetailModel;
import net.javango.carcare.model.ServiceListModel;

public class ServiceListFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";

    private static final int MENU_CONTEXT_EDIT_ID = 1;
    private static final int MENU_CONTEXT_DELETE_ID = 2;

    private AppDatabase database;
    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;

    /**
     * Creates an instance if this fragment.
     *
     * @param carId id of the {@code Car} for which to display services
     */
    public static ServiceListFragment newInstance(int carId) {
        ServiceListFragment fragment = new ServiceListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CAR_ID, carId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        database = AppDatabase.getDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_service, container, false);
        recyclerView = view.findViewById(R.id.service_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceAdapter = new ServiceAdapter(getActivity());
        recyclerView.setAdapter(serviceAdapter);

        int carId = getArguments().getInt(ARG_CAR_ID);
        CarDetailModel.getInstance(this, database, carId).getCar().observe(this, (car) -> setTitle(car));
        ServiceListModel.getInstance(this, database, carId).getServices()
                .observe(this, cars -> serviceAdapter.setData(cars));

        // Add Service FAB
        FloatingActionButton fabButton = view.findViewById(R.id.service_add_button);
        fabButton.setOnClickListener(v -> {
            Intent intent = ServiceDetailActivity.newIntent(getContext(), carId, null);
            startActivity(intent);
        });
        return view;
    }

    private void setTitle(Car car) {
        String title = car.getName(); // + " - " + getActivity().getString(R.string.service_history);
        getActivity().setTitle(title);
    }

}

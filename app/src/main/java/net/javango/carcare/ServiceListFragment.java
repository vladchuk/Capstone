package net.javango.carcare;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarDetailModel;
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceListModel;
import net.javango.carcare.util.TaskExecutor;

import java.util.List;

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
        database = AppDatabase.getDatabase(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_service, container, false);
        recyclerView = view.findViewById(R.id.service_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceAdapter = new ServiceAdapter(getActivity());
        recyclerView.setAdapter(serviceAdapter);

        int carId = getArguments().getInt(ARG_CAR_ID);
        CarDetailModel carModel = CarDetailModel.getInstance(this, database, carId);
        carModel.getCar().observe(this, (car) -> setTitle(car));

        ServiceListModel viewModel = ServiceListModel.getInstance(this, database, carId);
        viewModel.getServices().observe(this, cars -> serviceAdapter.setData(cars));

        // Add Service FAB
        FloatingActionButton fabButton = view.findViewById(R.id.service_add_button);
        fabButton.setOnClickListener(v -> {
            Intent intent = ServiceDetailActivity.newIntent(getContext(), carId,null);
            startActivity(intent);
        });
        return view;
    }

    private void setTitle(Car car) {
        String title = car.getName() + " - " + getActivity().getString(R.string.service_history);
        getActivity().setTitle(title);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        registerForContextMenu(getView());
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.fragment_car_list, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.new_car:
//                Intent intent = ServiceDetailActivity.newIntent(getActivity(), null);
//                startActivity(intent);
//                return true;
//            case R.id.search_service_stations:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ID, Menu.NONE, getString(R.string.edit_label));
//        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, getString(R.string.delete_label));
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        Service service = (Service) serviceAdapter.get(info.position);
//        switch (item.getItemId()) {
//            case MENU_CONTEXT_EDIT_ID:
//                Intent intent = ServiceDetailActivity.newIntent(getActivity(), service.getId());
//                startActivity(intent);
//                return true;
//            case MENU_CONTEXT_DELETE_ID:
//                TaskExecutor
//                        .executeDisk(() -> AppDatabase.getDatabase(getContext()).serviceDao().deleteService(service));
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

}

package net.javango.carcare;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
import net.javango.carcare.model.Service;
import net.javango.carcare.model.ServiceListModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;

import java.util.List;

public class ServiceListFragment extends ListFragment {

    private static final String ARG_CAR_ID = "car_id";

    private static final int MENU_CONTEXT_EDIT_ID = 1;
    private static final int MENU_CONTEXT_DELETE_ID = 2;

    private AppDatabase database;

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

        final ServiceAdapter adapter = new ServiceAdapter(getActivity());
        ServiceListModel viewModel = ServiceListModel.getInstance(this, database, -1);
        viewModel.getServices().observe(this, new Observer<List<Service>>() {
            @Override
            public void onChanged(@Nullable List<Service> cars) {
                adapter.setData(cars);
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_car_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_car:
                Intent intent = ServiceDetailActivity.newIntent(getActivity(), null);
                startActivity(intent);
                return true;
            case R.id.search_service_stations:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ID, Menu.NONE, getString(R.string.edit_label));
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, getString(R.string.delete_label));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Service service = (Service) getListAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case MENU_CONTEXT_EDIT_ID:
                Intent intent = ServiceDetailActivity.newIntent(getActivity(), service.getId());
                startActivity(intent);
                return true;
            case MENU_CONTEXT_DELETE_ID:
                TaskExecutor.executeDisk(() -> AppDatabase.getDatabase(getContext()).serviceDao().deleteService(service));
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Service car = (Service) getListAdapter().getItem(position);
        Intent intent = ServiceDetailActivity.newIntent(getActivity(), car.getId());
        startActivity(intent);
    }

    public static class ServiceAdapter extends ArrayAdapter<Service> {

        private List<Service> list;
        private final Activity context;

        public ServiceAdapter(Activity context) {
            super(context, R.layout.item_car);
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Nullable
        @Override
        public Service getItem(int position) {
            return list == null ? null : list.get(position);
        }

        public void setData(List<Service> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        static class ViewHolder {
            protected TextView name;
            protected TextView year;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                view = inflater.inflate(R.layout.item_car, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = view.findViewById(R.id.car_name);
                viewHolder.year = view.findViewById(R.id.car_year);
                view.setTag(viewHolder);
            } else {
                view = convertView;
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            Service service = list.get(position);
//            holder.name.setText(service.getName());
//            holder.year.setText(Formatter.toString(service.getModelYear()));
            return view;
        }
    }


}

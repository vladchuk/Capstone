package net.javango.carcare;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarListModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;
import net.javango.carcare.util.Util;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.List;

public class CarListFragment extends ListFragment {

    private static final int MENU_CONTEXT_EDIT_ID = 1;
    private static final int MENU_CONTEXT_DELETE_ID = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final CarAdapter adapter = new CarAdapter(getActivity());
        CarListModel viewModel = CarListModel.getInstance(this);
        viewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                adapter.setData(cars);
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //registerForContextMenu(getListView());
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = (Car) getListAdapter().getItem(position);
                Intent intent = CarDetailActivity.newIntent(getActivity(), car.getId());
                startActivity(intent);
                return true;
            }
        });
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
                Intent intent = CarDetailActivity.newIntent(getActivity(), null);
                startActivity(intent);
                return true;
            case R.id.export_data:
                exportData();
            case R.id.import_data:
                importData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Open GM with local service stations
     */
    private void openSearch() {
        Uri uri = Uri.parse("geo:0,0?q=mechanic");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent intent = new Intent(getContext(), SearchActivity.class);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No location app installed!", Toast.LENGTH_LONG).show();
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
        Car car = (Car) getListAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case MENU_CONTEXT_EDIT_ID:
                Intent intent = CarDetailActivity.newIntent(getActivity(), car.getId());
                startActivity(intent);
                return true;
            case MENU_CONTEXT_DELETE_ID:
                TaskExecutor.executeDisk(() -> AppDatabase.getDatabase(getContext()).carDao().deleteCar(car));
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Car car = (Car) getListAdapter().getItem(position);
        Intent intent = ServiceListActivity.newIntent(getActivity(), car.getId());
        startActivity(intent);
    }

    public static class CarAdapter extends ArrayAdapter<Car> {

        private List<Car> list;
        private final Activity context;

        public CarAdapter(Activity context) {
            super(context, R.layout.item_car);
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Nullable
        @Override
        public Car getItem(int position) {
            return list == null ? null : list.get(position);
        }

        public void setData(List<Car> list) {
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
            Car car = list.get(position);
            holder.name.setText(car.getName());
            holder.year.setText(Formatter.format(car.getModelYear()));
            return view;
        }
    }

    private void exportData() {
        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int permission = ActivityCompat.checkSelfPermission(getContext(), perm);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            AppDatabase.getDatabase(getContext()).checkpoint();

            File dbFile = AppDatabase.getDatabase(getContext()).getPath();
            File backupFile = new File("/sdcard/", "my-db");

            try {
                Util.copy(dbFile, backupFile);
            } catch (Exception e) {
                Log.e("SAVEDB", e.toString());
                Toast.makeText(getContext(), "Failed to copy data!", Toast.LENGTH_LONG);
            }
        }
        else {
            requestStoragePermission(perm);
        }
    }

    private void importData() {
        String perm = Manifest.permission.READ_EXTERNAL_STORAGE;
        int permission = ActivityCompat.checkSelfPermission(getContext(), perm);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            AppDatabase.getDatabase(getContext()).checkpoint();

            File backupFile = new File("/sdcard/", "my-db");
            File dbFile = AppDatabase.getDatabase(getContext()).getPath();

            try {
                Util.copy(backupFile, dbFile);
            } catch (Exception e) {
                Log.e("RESTOREDB", e.toString());
                Toast.makeText(getContext(), "Failed to copy data!", Toast.LENGTH_LONG);
            }
        }
        else {
            requestStoragePermission(perm);
        }
    }

    private void requestStoragePermission(String permission) {
//        Snackbar.make(getView(), "Please allow access to your storage", Snackbar.LENGTH_INDEFINITE)
//                .setAction("Allow", view -> ActivityCompat.requestPermissions(getActivity(), new String[]{
//                        permission
//                }, 0)).show();
        requestPermissions(new String[]{permission}, 13);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_CODE);
    }

}

package net.javango.carcare;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarListModel;

import java.util.List;

public class CarListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_car_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_car:
                Intent intent = CarActivity.newIntent(getActivity(), null);
                startActivity(intent);
                return true;
            case R.id.search_service_stations:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Car car = (Car) getListAdapter().getItem(position);
        Intent intent = CarActivity.newIntent(getActivity(), car.getId());
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
            holder.year.setText(String.valueOf(car.getModelYear()));
            return view;
        }
    }


}

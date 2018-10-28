package net.javango.carcare;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarDetailModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;
import net.javango.common.comp.TwoChoiceDialog;

/**
 * Handles {@code Car} UI. Used to insert, update or delete cars.
 */
public class CarDetailFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";

    private EditText name;
    private EditText year;
    private EditText tire;
    private AppDatabase database;
    private Integer carId;

    /**
     * Creates an instance if this fragment.
     *
     * @param carId id of the {@code Car} to display and process in this fragment. If {@code null}, it means a new
     *              car is being added
     */
    public static CarDetailFragment newInstance(Integer carId) {
        CarDetailFragment fragment = new CarDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CAR_ID, carId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(getActivity());
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.car_details);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_car_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.car_save:
                saveCar();
                getActivity().finish();
                return true;
            case R.id.car_delete:
                showDeleteDialg();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCar() {
        if (carId != null)
            TaskExecutor.executeDisk(() -> AppDatabase.getDatabase(getContext()).carDao().deleteById(carId));
    }

    private void showDeleteDialg() {
        TwoChoiceDialog dialog = TwoChoiceDialog
                .create(this, R.string.delete, R.string.car_delete_message, new TwoChoiceDialog.ChoiceListener() {

                    @Override
                    public void onPositiveChoice() {
                        deleteCar();
                        getActivity().finish();
                    }
                });
        dialog.show(getFragmentManager(), "delete-car-confirm");
    }

    private void setupView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            carId = (Integer) getArguments().getSerializable(ARG_CAR_ID);
            if (carId != null) {
                final CarDetailModel viewModel = CarDetailModel.getInstance(this, database, carId);
                viewModel.getCar().observe(this, new Observer<Car>() {
                    @Override
                    public void onChanged(@Nullable Car car) {
                        viewModel.getCar().removeObserver(this);
                        populateUI(car);
                    }
                });
            }
        } else {
            carId = (Integer) savedInstanceState.getSerializable(ARG_CAR_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_detail, container, false);

        name = v.findViewById(R.id.car_name_value);
        year = v.findViewById(R.id.car_year_value);
        tire = v.findViewById(R.id.car_tire_value);

        setupView(savedInstanceState);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_CAR_ID, carId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(Car car) {
        name.setText(car.getName());
        year.setText(Formatter.format(car.getModelYear()));
        tire.setText(car.getTireSize());
    }

    /**
     * Saves new car data into the underlying database.
     */
    private void saveCar() {
        final Car car = new Car();
        car.setName(name.getText().toString());
        Integer y = Formatter.parseInt(year.getText().toString());
        car.setModelYear(y);
        car.setTireSize(tire.getText().toString());

        TaskExecutor.executeDisk(new Runnable() {
            @Override
            public void run() {
                if (carId == null) {
                    // insert
                    database.carDao().addCar(car);
                } else {
                    //update
                    car.setId(carId);
                    database.carDao().updateCar(car);
                }
                getActivity().finish();
            }
        });
    }

}

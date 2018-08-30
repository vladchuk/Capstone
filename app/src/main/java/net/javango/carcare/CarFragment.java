package net.javango.carcare;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import net.javango.carcare.model.Car;
import net.javango.carcare.model.CarModel;
import net.javango.carcare.util.Formatter;
import net.javango.carcare.util.TaskExecutor;

import java.util.Date;

/**
 * Handles {@code Car} UI. Used to insert, update or delete cars.
 */
public class CarFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";

    private EditText name;
    private EditText year;
    private EditText tire;
    private Button cancel;
    private Button save;
    private AppDatabase database;
    private Integer carId;

    /**
     * Creates an instance if this fragment.
     *
     * @param carId id of the {@code Car} to display and process in this fragment. If {@code null}, it means a new
     *              car is being added
     */
    public static CarFragment newInstance(Integer carId) {
        CarFragment fragment = new CarFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car, container, false);

        name = v.findViewById(R.id.car_name_value);
        year = v.findViewById(R.id.car_year_value);
        tire = v.findViewById(R.id.car_tire_value);
        cancel = v.findViewById(R.id.car_cancel_button);
        cancel.setOnClickListener(view -> getActivity().finish());
        save = v.findViewById(R.id.car_save_button);
        save.setOnClickListener(view -> onSaveButtonClicked());

        carId = (Integer) getArguments().getSerializable(ARG_CAR_ID);
        if (carId != null) {
//            mButton.setText(R.string.update_button);
                // populate the UI
                final CarModel viewModel = CarModel.getInstance(this, database, carId);

                viewModel.getCar().observe(this, new Observer<Car>() {
                    @Override
                    public void onChanged(@Nullable Car car) {
                        viewModel.getCar().removeObserver(this);
                        populateUI(car);
                    }
                });
            }

        return v;
    }

    private void populateUI(Car car) {
        name.setText(car.getName());
        year.setText(String.valueOf(car.getModelYear()));
        tire.setText(car.getTireSize());
    }

    /**
     * Inserts that new car data into the underlying database.
     */
    public void onSaveButtonClicked() {
        final Car car = new Car();
        car.setName(name.getText().toString());
        Integer y = Formatter.parseInt(year.getText().toString());
        if (y != null)
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
                }
                getActivity().finish();
            }
        });
    }
}

package net.javango.carcare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CarFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";

    private EditText name;
    private EditText year;
    private EditText tire;


    public static CarFragment newInstance(int carId) {
        CarFragment fragment = new CarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CAR_ID, carId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        return v;
    }
}

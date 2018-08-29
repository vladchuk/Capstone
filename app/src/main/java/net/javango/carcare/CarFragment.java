package net.javango.carcare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import net.javango.carcare.model.Car;

public class CarFragment extends Fragment {

    private static final String ARG_CAR_ID = "car_id";

    public static CarFragment newInstance(int carId) {
        CarFragment fragment = new CarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CAR_ID, carId);
        fragment.setArguments(bundle);
        return fragment;
    }

}

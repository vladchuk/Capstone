package net.javango.carcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.javango.carcare.model.Car;

public class CarActivity extends SingleFragmentActivity {

    private final static String EXTRA_CAR_ID = "net.javango.carcare.car_id";

    @Override
    protected Fragment createFragment() {
        int carId = getIntent().getIntExtra(EXTRA_CAR_ID, -1);
        return CarFragment.newInstance(carId);
    }

    public static Intent newIntent(Context context, int carId) {
        Intent intent = new Intent(context, CarActivity.class);
        intent.putExtra(EXTRA_CAR_ID, carId);
        return intent;
    }
}

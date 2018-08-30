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

/**
 * Hosts a {@code CarFragment}
 */
public class CarActivity extends SingleFragmentActivity {

    private final static String EXTRA_CAR_ID = "net.javango.carcare.car_id";

    @Override
    protected Fragment createFragment() {
        Integer carId = (Integer) getIntent().getSerializableExtra(EXTRA_CAR_ID);
        return CarFragment.newInstance(carId);
    }

    /**
     * Creates an intent to open this activity
     * @param carId if {@code null}, a new car will be created
     */
    public static Intent newIntent(Context context, Integer carId) {
        Intent intent = new Intent(context, CarActivity.class);
        intent.putExtra(EXTRA_CAR_ID, carId);
        return intent;
    }
}

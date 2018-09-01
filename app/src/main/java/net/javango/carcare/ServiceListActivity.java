package net.javango.carcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import net.javango.carcare.CarListFragment;
import net.javango.carcare.SingleFragmentActivity;

public class ServiceListActivity extends SingleFragmentActivity {

    private final static String EXTRA_CAR_ID = "net.javango.carcare.car_id";

    @Override
    protected Fragment createFragment() {
        Integer carId = (Integer) getIntent().getSerializableExtra(EXTRA_CAR_ID);
        return ServiceListFragment.newInstance(carId);
    }

    /**
     * Creates an intent to open this activity
     * @param carId identifies the parent of services
     */
    public static Intent newIntent(Context context, Integer carId) {
        Intent intent = new Intent(context, CarDetailActivity.class);
        intent.putExtra(EXTRA_CAR_ID, carId);
        return intent;
    }

}

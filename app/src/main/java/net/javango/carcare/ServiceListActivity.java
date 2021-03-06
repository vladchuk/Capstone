package net.javango.carcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

public class ServiceListActivity extends SingleFragmentActivity {

    private final static String EXTRA_CAR_ID = "net.javango.carcare.car_id";

    @Override
    protected Fragment createFragment() {
        int carId = getIntent().getIntExtra(EXTRA_CAR_ID, -1);
        if (carId  > 0)
            saveCarId(carId);
        else
            carId = getCarId();
        return ServiceListFragment.newInstance(carId);
    }

    /**
     * Creates an intent to open this activity
     * @param carId identifies the parent of services
     */
    public static Intent newIntent(Context context, int carId) {
        Intent intent = new Intent(context, ServiceListActivity.class);
        intent.putExtra(EXTRA_CAR_ID, carId);
        return intent;
    }

    public static Intent newFillInIntent(int carId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CAR_ID, carId);
        return intent;
    }

    // required to support up navigation
    private int getCarId() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        return prefs.getInt(EXTRA_CAR_ID, -1);
    }

    private void saveCarId(int id) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(EXTRA_CAR_ID, id);
        editor.commit();
    }

}

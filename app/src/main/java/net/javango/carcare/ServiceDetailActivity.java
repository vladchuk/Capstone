package net.javango.carcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import net.javango.carcare.CarDetailFragment;
import net.javango.carcare.SingleFragmentActivity;

/**
 * Hosts a {@code CarDetailFragment}
 */
public class ServiceDetailActivity extends SingleFragmentActivity {

    private final static String EXTRA_SERVICE_ID = "net.javango.carcare.service_id";

    @Override
    protected Fragment createFragment() {
        Integer serviceId = (Integer) getIntent().getSerializableExtra(EXTRA_SERVICE_ID);
        return CarDetailFragment.newInstance(serviceId);
    }

    /**
     * Creates an intent to open this activity
     * @param serviceId if {@code null}, a new service will be created
     */
    public static Intent newIntent(Context context, Integer serviceId) {
        Intent intent = new Intent(context, ServiceDetailActivity.class);
        intent.putExtra(EXTRA_SERVICE_ID, serviceId);
        return intent;
    }
}

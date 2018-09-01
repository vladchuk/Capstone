package net.javango.carcare;

import android.support.v4.app.Fragment;

import net.javango.carcare.CarListFragment;
import net.javango.carcare.SingleFragmentActivity;

public class ServiceListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ServiceListFragment();
    }

}

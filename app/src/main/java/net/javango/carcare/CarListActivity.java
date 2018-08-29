package net.javango.carcare;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CarListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CarListFragment();
    }

}

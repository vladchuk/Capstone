package net.javango.carcare.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.javango.carcare.R;
import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Car;

/**
 * The configuration screen for the {@link Widget Widget} AppWidget.
 */
public class WidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "net.javango.carecare.widget.Widget";
    private static final String PREF_PREFIX_KEY = "widget_";
    private static final String PREF_PREFIX_NAME = "widget_name";
    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner carChoice;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_configure);

        carChoice = findViewById(R.id.car_choice);
        AppDatabase.getDatabase(this).carDao().getAll().observe(this, cars -> {
            ArrayAdapter<Car> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cars);
            carChoice.setAdapter(adapter);
        });

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            Car car = (Car) carChoice.getSelectedItem();
            saveWidgetPref(context, widgetId, car);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Widget.updateAppWidget(context, appWidgetManager, widgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.list_view);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public WidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveWidgetPref(Context context, int appWidgetId, Car car) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, car.getId());
        prefs.putString(PREF_PREFIX_NAME + appWidgetId, car.getName());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int getCarId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int rid = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return rid;
    }

    static String getCarName(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String name = prefs.getString(PREF_PREFIX_NAME + appWidgetId, null);
        return name;
    }

    static void deleteWidgetPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(PREF_PREFIX_NAME + appWidgetId);
        prefs.apply();
    }

}


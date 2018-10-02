package net.javango.carcare.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.javango.carcare.R;
import net.javango.carcare.ServiceListActivity;
import net.javango.carcare.model.AppDatabase;
import net.javango.carcare.model.Service;
import net.javango.carcare.util.Formatter;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    public static Intent newIntent(Context context, int carId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setData(Uri.fromParts("content", String.valueOf(carId), null));
        return intent;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }


    private static class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Service> serviceList;
        private Context context;
        private int carId;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            carId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        }

        @Override
        public void onCreate() {
            // empty
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getViewTypeCount() {
            return 1; // all items in the view are the same
        }

        @Override
        public int getCount() {
            return serviceList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public void onDestroy() {
            // empty
        }

        @Override
        public void onDataSetChanged() {
            serviceList = AppDatabase.getDatabase(context).serviceDao().getForCarSync(carId);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.widget_item_service);
            Service service = serviceList.get(position);
            remoteView.setTextViewText(R.id.widget_item_description, service.getDescription());
            remoteView.setTextViewText(R.id.widget_item_date, Formatter.format(service.getDate()));

            Intent fillInIntent = ServiceListActivity.newFillInIntent(carId);
            remoteView.setOnClickFillInIntent(R.id.widget_item_description, fillInIntent);
            return remoteView;
        }

    }
}
package net.javango.carcare;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.javango.carcare.model.Service;
import net.javango.carcare.util.Formatter;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> list;
    private final Activity context;

    public ServiceAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_service, parent,false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = list.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<Service> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView descriptionView;
        private TextView dateView;
        private TextView costView;
        private TextView mileageView;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.service_item_description);
            dateView = itemView.findViewById(R.id.service_item_date);
            costView = itemView.findViewById(R.id.service_item_cost);
            mileageView = itemView.findViewById(R.id.service_item_mileage);
            itemView.setOnClickListener(this);
        }

        private void bind(Service service) {
            descriptionView.setText(service.getDescription());
//            dateView.setText(service.getDate().toString());
            costView.setText(Formatter.toString(service.getCost()));
            mileageView.setText(Formatter.toString(service.getMileage()));
        }

        @Override
        public void onClick(View v) {

        }
    }

}

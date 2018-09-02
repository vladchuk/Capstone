package net.javango.carcare;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

    class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView descriptionView;
        private TextView dateView;
        private TextView costView;
        private TextView mileageView;
        private Service service;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.service_item_description);
            dateView = itemView.findViewById(R.id.service_item_date);
            costView = itemView.findViewById(R.id.service_item_cost);
            mileageView = itemView.findViewById(R.id.service_item_mileage);
            itemView.setOnClickListener(this);
        }

        private void bind(Service service) {
            this.service = service;
            descriptionView.setText(service.getDescription());
            dateView.setText(Formatter.format(service.getDate()));

            Integer miles = service.getMileage();
            String mileage = miles == null ? "" :  Formatter.format(service.getMileage()) + " " + context.getString(R.string.miles);
            mileageView.setText(mileage);

            Integer cost = service.getCost();
            String costStr = cost == null ? "" : "$" + Formatter.format(service.getCost());
            costView.setText(costStr);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ServiceDetailActivity.newIntent(context, service.getCarId(), service.getId());
            context.startActivity(intent);
        }
    }

}

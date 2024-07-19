package com.example.policia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {
    private List<Incident> incidentList;
    private Context context;

    public IncidentAdapter(List<Incident> incidentList, Context context) {
        this.incidentList = incidentList;
        this.context = context;
    }

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent, false);
        return new IncidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {
        Incident incident = incidentList.get(position);
        holder.title.setText(incident.getTitle());
        holder.date.setText(incident.getDate());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, IncidentDetailActivity.class);
            intent.putExtra("incident", incident);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public class IncidentViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;

        public IncidentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}

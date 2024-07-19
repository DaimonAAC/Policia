package com.example.policia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.policia.AppDatabase;
import com.example.policia.Incident;

import java.util.List;


public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private IncidentAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "incident_db").allowMainThreadQueries().build();

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Incident> incidents = db.incidentDao().getAllIncidents();
        adapter = new IncidentAdapter(incidents, getContext());
        recyclerView.setAdapter(adapter);

        return root;
    }
}

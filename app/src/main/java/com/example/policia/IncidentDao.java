package com.example.policia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncidentDao {
    @Insert
    void insert(Incident incident);

    @Query("SELECT * FROM incidents")
    List<Incident> getAllIncidents();
}

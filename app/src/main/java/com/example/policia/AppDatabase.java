package com.example.policia;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Incident.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IncidentDao incidentDao();
}

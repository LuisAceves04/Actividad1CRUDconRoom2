package com.fic.actividad1crudconroom.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

@Database(
        entities = {Task.class},
        version = 1
)
@TypeConverters({Converters.class})
public abstract class Appdatabase extends RoomDatabase {

    private static volatile Appdatabase INSTANCE;

    public static Appdatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Appdatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),Appdatabase.class, "tasks.db").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract Taskdao taskDao();
}
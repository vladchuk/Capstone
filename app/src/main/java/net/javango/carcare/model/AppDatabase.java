package net.javango.carcare.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.graphics.Movie;

import net.javango.carcare.util.DateConverter;

@Database(entities = {Car.class, Service.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String NAME = "CARCARE_DB";
    private static AppDatabase DB;

    public abstract CarDao carDao();

    public abstract ServiceDao serviceDao();

    public static AppDatabase getDatabase(Context context) {
        if (DB == null) {
            DB = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                    .build();
        }
        return DB;
    }

}

package net.javango.carcare.model;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;

import net.javango.carcare.util.DateConverter;

import java.io.File;

@Database(entities = {Car.class, Service.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String NAME = "CARCARE_DB";

    private static AppDatabase DB;
    private static File dbPath;

    public abstract CarDao carDao();

    public abstract ServiceDao serviceDao();

    public static AppDatabase getDatabase(Context context) {
        if (DB == null) {
            dbPath = context.getDatabasePath(NAME);
            DB = Room.databaseBuilder(context, AppDatabase.class, NAME)
                    .build();
        }
        return DB;
    }

    public void checkpoint() {
        try (Cursor c = DB.query("pragma wal_checkpoint(full)", null)) {
            if (c.moveToFirst() && c.getInt(0) == 1)
                throw new RuntimeException("Checkpoint was blocked from completing");
        }
    }

    public File getPath() {
        return dbPath;
    }

}

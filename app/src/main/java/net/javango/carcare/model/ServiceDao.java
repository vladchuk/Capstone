package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert
    void addService(Service Service);

    @Delete
    void deleteService(Service Service);

    @Update
    void updateService(Service Service);

    @Query(value = "select * from Service where id = :id")
    Service findById(int id);

    @Query(value = "select * from Service order by date desc")
    LiveData<List<Service>> getAll();

}

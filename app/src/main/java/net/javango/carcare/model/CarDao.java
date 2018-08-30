package net.javango.carcare.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import net.javango.carcare.util.DateConverter;

import java.util.List;

@Dao
public interface CarDao {
    @Insert
    void addCar(Car car);

    @Delete
    void deleteCar(Car car);

    @Update
    void updateCar(Car car);

    @Query(value = "select * from car where id = :id")
    LiveData<Car> findById(int id);

    @Query(value = "select * from car order by name")
    LiveData<List<Car>> getAll();

}

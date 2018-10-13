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

    @Query(value = "delete from service where id = :id")
    void deleteById(int id);

    @Update
    void updateService(Service Service);

    @Query(value = "select * from Service where id = :id")
    LiveData<Service> findById(int id);

    static final String CAR_SERVICES = "select * from Service where carId = :carId order by date desc";

    @Query(value = CAR_SERVICES)
    LiveData<List<Service>> getForCar(int carId);

    @Query(value = CAR_SERVICES)
    List<Service> getForCarSync(int carId);

}

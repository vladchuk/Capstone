package net.javango.carcare.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Represents a vehicle in user's garage
 */
@Entity
//(indices = {@Index(value = {"name"}, unique = true)})
public class Car {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Integer modelYear;
    private String tireSize;

    public Car() {
        // required by room
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTireSize() {
        return tireSize;
    }

    public void setTireSize(String tireSize) {
        this.tireSize = tireSize;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }
}

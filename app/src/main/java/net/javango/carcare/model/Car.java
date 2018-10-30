package net.javango.carcare.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Represents a vehicle in user's garage
 */
@Entity
public class Car {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String model;
    private Integer modelYear;
    private String licensePlate;
    private String tireSize;
    private String trim;
    private String notes;

    public Car() {
        // required by room
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String toString() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

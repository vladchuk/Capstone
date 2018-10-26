package net.javango.carcare.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * A service (maintenance) done to a car
 */
@Entity(
        indices = @Index("carId"),
        foreignKeys =
        @ForeignKey(
                entity = Car.class,
                parentColumns = "id",
                childColumns = "carId",
                onDelete = CASCADE)
)
public class Service {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    // optional
    private Integer mileage;
    private String description;
    // optional, whole dollars
    private Integer cost;
    private String location;
    private String notes;

    private int carId;

    public Service(int carId) {
        this.carId = carId;
        // required by Room
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}

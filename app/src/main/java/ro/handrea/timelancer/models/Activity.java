package ro.handrea.timelancer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created on 11/24/17.
 */

@Entity
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public Activity(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

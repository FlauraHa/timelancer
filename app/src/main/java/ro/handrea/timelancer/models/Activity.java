package ro.handrea.timelancer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ro.handrea.timelancer.database.AppDatabase;

/**
 * Created on 11/24/17.
 */

@Entity
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Activity(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static Activity getFor(AppDatabase db, TimeLog timeLog) {
        return db.activityDao().getEntryFor(timeLog);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

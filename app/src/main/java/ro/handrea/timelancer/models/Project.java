package ro.handrea.timelancer.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.Executor;

import ro.handrea.timelancer.database.AppDatabase;

/**
 * Created on 11/24/17.
 */

@Entity
public class Project {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Project(String name) {
        this.name = name;
    }

    public static Project getFor(AppDatabase db, TimeLog timeLog) {
        return db.projectDao().getEntryFor(timeLog);
    }

    public void setId(long id) {
        this.id = id;
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

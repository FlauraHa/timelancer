package ro.handrea.timelancer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

import ro.handrea.timelancer.database.AppDatabase;

/**
 * Created on 11/25/17.
 */

@Entity
public class WorkTime {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int hours;
    private int minutes;

    public WorkTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        // TODO: Manage over 60 minutes case
    }

    public static WorkTime getFor(AppDatabase db, TimeLog timeLog) {
        return db.workTimeDao().getEntryFor(timeLog);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof WorkTime) {
            WorkTime targetWorkHours = (WorkTime) target;

            return targetWorkHours.getHours() == hours && targetWorkHours.getMinutes() == minutes;
        }

        return false;
    }
}

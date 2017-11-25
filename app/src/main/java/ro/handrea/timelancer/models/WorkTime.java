package ro.handrea.timelancer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

/**
 * Created on 11/25/17.
 */

@Entity
public class WorkTime {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "hours")
    private int mHours;

    @ColumnInfo(name = "minutes")
    private int mMinutes;

    public WorkTime(int hours, int minutes) {
        this.mHours = hours;
        this.mMinutes = minutes;
        // TODO: Manage over 60 minutes case
    }

    public int getId() {
        return mId;
    }

    public int getHours() {
        return mHours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d:%02d", mHours, mMinutes);
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof WorkTime) {
            WorkTime targetWorkHours = (WorkTime) target;

            return targetWorkHours.getHours() == mHours && targetWorkHours.getMinutes() == mMinutes;
        }

        return false;
    }
}

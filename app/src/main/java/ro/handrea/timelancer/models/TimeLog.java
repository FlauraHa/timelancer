package ro.handrea.timelancer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created on 11/24/17.
 */

@Entity
public class TimeLog {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "date")
    private Date mDate;

    @ColumnInfo(name = "project_id")
    private int mProjectId;

    @ColumnInfo(name = "activity_id")
    private int mActivityId;

    @ColumnInfo(name = "work_time_id")
    private int mWorkTimeId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public void setProjectId(int projectId) {
        this.mProjectId = projectId;
    }

    public int getActivityId() {
        return mActivityId;
    }

    public void setActivityId(int activityId) {
        this.mActivityId = activityId;
    }

    public int getWorkTimeId() {
        return mWorkTimeId;
    }

    public void setWorkTimeId(int workTimeId) {
        this.mWorkTimeId = workTimeId;
    }
}

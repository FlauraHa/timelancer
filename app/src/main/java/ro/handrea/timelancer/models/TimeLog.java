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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private Date date;
    private int projectId;
    private int activityId;
    private int workTimeId;

    public TimeLog(Date date, int projectId, int activityId, int workTimeId) {
        this.date = date;
        this.projectId = projectId;
        this.activityId = activityId;
        this.workTimeId = workTimeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getActivityId() {
        return activityId;
    }

    public int getWorkTimeId() {
        return workTimeId;
    }
}

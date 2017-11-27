package ro.handrea.timelancer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created on 11/24/17.
 */

@Entity
public class TimeLog implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    private Date date;
    private long projectId;
    private long activityId;
    private long workTimeId;

    public TimeLog(Date date, long projectId, long activityId, long workTimeId) {
        this.date = date;
        this.projectId = projectId;
        this.activityId = activityId;
        this.workTimeId = workTimeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getActivityId() {
        return activityId;
    }

    public long getWorkTimeId() {
        return workTimeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date.getTime());
        dest.writeLong(projectId);
        dest.writeLong(activityId);
        dest.writeLong(workTimeId);
    }

    private TimeLog(Parcel in) {
        id = in.readLong();
        date = new Date(in.readLong());
        projectId = in.readLong();
        activityId = in.readLong();
        workTimeId = in.readLong();
    }

    public static final Parcelable.Creator<TimeLog> CREATOR
            = new Parcelable.Creator<TimeLog>() {
        public TimeLog createFromParcel(Parcel in) {
            return new TimeLog(in);
        }

        public TimeLog[] newArray(int size) {
            return new TimeLog[size];
        }
    };
}

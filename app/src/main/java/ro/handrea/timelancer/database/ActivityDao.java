package ro.handrea.timelancer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.models.TimeLog;

/**
 * Created on 11/24/17.
 */

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity WHERE id = :activityId")
    Activity findById(long activityId);

    @Query("SELECT * FROM activity")
    List<Activity> loadAllActivities();

    @Query("SELECT * FROM activity, timelog where timelog.activityId = activity.id and timelog.id = :timeLog")
    Activity getEntryFor(TimeLog timeLog);

    @Insert
    void insert(Activity activity);
}

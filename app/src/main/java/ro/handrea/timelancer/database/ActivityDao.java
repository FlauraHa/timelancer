package ro.handrea.timelancer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import ro.handrea.timelancer.models.Activity;

/**
 * Created on 11/24/17.
 */

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activity);

    @Query("SELECT * FROM activity WHERE id = :activityId")
    Activity findById(int activityId);
}

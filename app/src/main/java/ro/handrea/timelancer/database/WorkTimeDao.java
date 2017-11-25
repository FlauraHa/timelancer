package ro.handrea.timelancer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import ro.handrea.timelancer.models.WorkTime;

/**
 * Created on 11/25/17.
 */

@Dao
public interface WorkTimeDao {
    @Query("SELECT * FROM worktime WHERE id = :workTimeId")
    WorkTime findById(int workTimeId);

    @Insert
    void insert(WorkTime workTime);
}

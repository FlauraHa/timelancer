package ro.handrea.timelancer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import ro.handrea.timelancer.models.TimeLog;

/**
 * Created on 11/24/17.
 */

@Dao
public interface TimeLogDao {
    @Query("SELECT * FROM timelog WHERE date = :date")
    List<TimeLog> findByDate(Date date);

    @Insert
    void insert(TimeLog timeLog);
}

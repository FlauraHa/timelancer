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
    @Query("SELECT * FROM timelog WHERE date BETWEEN :dateStart and :dateEnd")
    List<TimeLog> findByDate(Date dateStart, Date dateEnd);

    @Insert
    long insert(TimeLog timeLog);
}

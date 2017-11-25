package ro.handrea.timelancer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import ro.handrea.timelancer.models.Project;

/**
 * Created on 11/24/17.
 */

@Dao
public interface ProjectDao {
    @Insert
    void insert(Project project);

    @Query("SELECT * FROM project WHERE id = :projectId")
    Project findById(int projectId);
}

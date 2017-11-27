package ro.handrea.timelancer.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.models.TimeLog;

/**
 * Created on 11/24/17.
 */

@Dao
public interface ProjectDao {
    @Insert
    void insert(Project project);

    @Query("SELECT * FROM project WHERE id = :projectId")
    Project findById(long projectId);

    @Query("SELECT * FROM project")
    List<Project> loadAllProjects();

    @Query("SELECT * FROM project, timelog where timelog.projectId = project.id and timelog.id = :timeLog")
    Project getEntryFor(TimeLog timeLog);
}

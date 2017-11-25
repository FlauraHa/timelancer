package ro.handrea.timelancer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.models.TimeLog;
import ro.handrea.timelancer.models.WorkTime;

/**
 * Created on 11/24/17.
 */

@Database(entities = {TimeLog.class, Project.class, Activity.class, WorkTime.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "databaseTimeLancer";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract TimeLogDao timeLogDao();

    public abstract ProjectDao projectDao();

    public abstract ActivityDao activityDao();

    public abstract WorkTimeDao workTimeDao();
}
package ro.handrea.timelancer.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

import ro.handrea.timelancer.models.TimeLog;

/**
 * Created on 11/24/17.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static long timeLogToId(TimeLog timeLog) {
        return timeLog.getId();
    }
}

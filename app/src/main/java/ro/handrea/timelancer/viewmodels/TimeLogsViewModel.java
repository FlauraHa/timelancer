package ro.handrea.timelancer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.TimeLog;

/**
 * Created on 11/23/17.
 */

public class TimeLogsViewModel extends ViewModel {

    private AppDatabase mDatabase;
    private Executor mExecutor;
    private MutableLiveData<List<TimeLog>> mTimeLogs;

    public TimeLogsViewModel(AppDatabase database, Executor executor) {
        mDatabase = database;
        mExecutor = executor;
        mTimeLogs = new MutableLiveData<>();
    }

    public void addTimeLog(TimeLog timeLog) {
        mTimeLogs.getValue().add(timeLog);
        mExecutor.execute(() -> mDatabase.timeLogDao().insert(timeLog));
    }

    public LiveData<List<TimeLog>> filterTimeLogsBy(final Date date) {
        Date startOfDay = getStartOfDayFor(date);
        Date endOfDay = getEndOfDayFor(date);

        mExecutor.execute(() -> mTimeLogs.postValue(mDatabase.timeLogDao().findByDate(startOfDay, endOfDay)));
        return mTimeLogs;
    }

    private Date getStartOfDayFor(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return new Date(cal.getTimeInMillis());
    }

    private Date getEndOfDayFor(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return new Date(cal.getTimeInMillis());
    }
}

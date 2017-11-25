package ro.handrea.timelancer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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
        mTimeLogs = new MutableLiveData<List<TimeLog>>();
    }

    public LiveData<List<TimeLog>> getTimeLogsFor(final Date date) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTimeLogs.postValue(mDatabase.timeLogDao().findByDate(date));
            }
        });

        return mTimeLogs;
    }
}

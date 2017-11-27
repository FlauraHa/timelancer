package ro.handrea.timelancer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Activity;

/**
 * Created on 11/25/17.
 */

public class ActivitiesViewModel extends ViewModel {
    private AppDatabase mDatabase;
    private Executor mExecutor;
    private MutableLiveData<List<Activity>> mActivities;

    public ActivitiesViewModel(AppDatabase database, Executor executor) {
        mDatabase = database;
        mExecutor = executor;
    }

    public LiveData<List<Activity>> getActivities() {
        if (mActivities == null) {
            mActivities = new MutableLiveData<>();
            mExecutor.execute(() -> mActivities.postValue(mDatabase.activityDao().loadAllActivities()));
        }
        return mActivities;
    }

    public void addActivity(final Activity activity) {
        mExecutor.execute(() -> {
            List<Activity> activities = mActivities.getValue();
            long activityId = mDatabase.activityDao().insert(activity);

            activity.setId(activityId);
            activities.add(activity);
            mActivities.postValue(activities);
        });
    }
}

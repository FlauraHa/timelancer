package ro.handrea.timelancer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.models.WorkTime;

/**
 * Created on 11/26/17.
 */

public class NewTimeEntryViewModel extends ViewModel {
    private AppDatabase mDatabase;
    private Executor mExecutor;
    private MutableLiveData<List<Project>> mProjects;
    private MutableLiveData<List<Activity>> mActivities;

    public NewTimeEntryViewModel(AppDatabase database, Executor executor) {
        mDatabase = database;
        mExecutor = executor;
        mProjects = new MutableLiveData<>();
        mActivities = new MutableLiveData<>();
    }

    public LiveData<List<Project>> getProjects() {
        mExecutor.execute(() -> mProjects.postValue(mDatabase.projectDao().loadAllProjects()));
        return mProjects;
    }

    public LiveData<List<Activity>> getActivities() {
        mExecutor.execute(() -> mActivities.postValue(mDatabase.activityDao().loadAllActivities()));
        return mActivities;
    }

    public LiveData<Long> addWorkTime(WorkTime workTime) {
        MutableLiveData<Long> workTimeId = new MutableLiveData<>();
        mExecutor.execute(() -> workTimeId.postValue(mDatabase.workTimeDao().insert(workTime)));
        return workTimeId;
    }
}

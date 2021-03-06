package ro.handrea.timelancer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Project;

/**
 * Created on 11/25/17.
 */

public class ProjectsViewModel extends ViewModel {
    private AppDatabase mDatabase;
    private Executor mExecutor;
    private MutableLiveData<List<Project>> mProjects;

    public ProjectsViewModel(AppDatabase database, Executor executor) {
        mDatabase = database;
        mExecutor = executor;
    }

    public LiveData<List<Project>> getProjects() {
        if (mProjects == null) {
            mProjects = new MutableLiveData<>();
            mExecutor.execute(() -> mProjects.postValue(mDatabase.projectDao().loadAllProjects()));
        }
        return mProjects;
    }

    public void addProject(final Project project) {
        mExecutor.execute(() -> {
            List<Project> projects = mProjects.getValue();
            long projectIt = mDatabase.projectDao().insert(project);

            project.setId(projectIt);
            projects.add(project);
            mProjects.postValue(projects);
        });
    }
}

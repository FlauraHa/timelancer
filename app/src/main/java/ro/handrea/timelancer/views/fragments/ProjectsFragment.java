package ro.handrea.timelancer.views.fragments;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.ThreadPerTaskExecutor;
import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.viewmodels.ProjectsViewModel;
import ro.handrea.timelancer.views.listeners.ProjectCreatedListener;
import ro.handrea.timelancer.views.listeners.ViewScrollListener;
import ro.handrea.timelancer.views.activities.MainActivity;
import ro.handrea.timelancer.views.adapters.ProjectsAdapter;
import ro.handrea.timelancer.views.dialogs.NewProjectDialog;

public class ProjectsFragment extends FabAwareFragment implements ProjectCreatedListener {
    private static final String TAG = ProjectsFragment.class.getSimpleName();

    private ProjectsViewModel mViewModel;
    private ProjectsAdapter mProjectsAdapter;
    private NewProjectDialog mNewProjectDialog;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.equals(ProjectsViewModel.class)) {
                    return (T) new ProjectsViewModel(AppDatabase.getInstance(getContext()),
                            new ThreadPerTaskExecutor());
                }
                // Needed for NonNull annotation
                return (T) new Object();
            }
        }).get(ProjectsViewModel.class);
        setAdaptersData();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_projects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mProjectsAdapter = new ProjectsAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mProjectsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // No operation
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Activity activity = getActivity();

                if (activity instanceof ViewScrollListener) {
                    MainActivity mainActivity = (MainActivity) activity;

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        mainActivity.onViewScrolled();
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mainActivity.onViewScrollStateIdle();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void setAdaptersData() {
        LiveData<List<Project>> projects = mViewModel.getProjects();
        projects.observe(ProjectsFragment.this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                Log.d(TAG, "projects from db are here");
                mProjectsAdapter.setData(projects);
            }
        });
    }

    @Override
    public void onFabClick() {
        showNewProjectDialog();
    }

    private void showNewProjectDialog() {
        if (mNewProjectDialog == null) {
            mNewProjectDialog = new NewProjectDialog(getActivity(), this);
        }
        mNewProjectDialog.show();
    }

    @Override
    public void onProjectCreated(Project project) {
        mViewModel.addProject(project);
    }

    @Override
    public void onDetach() {
        if (mNewProjectDialog != null && mNewProjectDialog.isShowing()) {
            mNewProjectDialog.dismiss();
        }
        super.onDetach();
    }
}

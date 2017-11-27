package ro.handrea.timelancer.views.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.ThreadPerTaskExecutor;
import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.viewmodels.ActivitiesViewModel;
import ro.handrea.timelancer.views.activities.MainActivity;
import ro.handrea.timelancer.views.adapters.ActivitiesAdapter;
import ro.handrea.timelancer.views.dialogs.NewActivityDialog;
import ro.handrea.timelancer.views.listeners.ActivityCreatedListener;
import ro.handrea.timelancer.views.listeners.ViewScrollListener;


public class ActivitiesFragment extends FabAwareFragment implements ActivityCreatedListener {
    private static final String TAG = ActivitiesFragment.class.getSimpleName();

    private ActivitiesViewModel mViewModel;
    private ActivitiesAdapter mActivitiesAdapter;
    private NewActivityDialog mNewActivityDialog;

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities, container, false);
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
                if (modelClass.equals(ActivitiesViewModel.class)) {
                    return (T) new ActivitiesViewModel(AppDatabase.getInstance(getContext()),
                            new ThreadPerTaskExecutor());
                }
                // Needed for NonNull annotation
                return (T) new Object();
            }
        }).get(ActivitiesViewModel.class);
        setAdaptersData();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_activities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mActivitiesAdapter = new ActivitiesAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mActivitiesAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // No operation
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                android.app.Activity activity = getActivity();

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
        LiveData<List<Activity>> activities = mViewModel.getActivities();
        activities.observe(ActivitiesFragment.this, activities1 -> mActivitiesAdapter.setData(activities1));
    }

    @Override
    public void onFabClick() {
        showNewActivityDialog();
    }

    private void showNewActivityDialog() {
        if (mNewActivityDialog == null) {
            mNewActivityDialog = new NewActivityDialog(getActivity(), this);
        }
        mNewActivityDialog.show();
    }

    @Override
    public void onActivityCreated(Activity activity) {
        mViewModel.addActivity(activity);
    }

    @Override
    public void onDetach() {
        if (mNewActivityDialog != null && mNewActivityDialog.isShowing()) {
            mNewActivityDialog.dismiss();
        }
        super.onDetach();
    }
}

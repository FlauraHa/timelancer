package ro.handrea.timelancer.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.ThreadPerTaskExecutor;
import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.R;
import ro.handrea.timelancer.models.TimeLog;
import ro.handrea.timelancer.views.activities.NewTimeEntryActivity;
import ro.handrea.timelancer.views.listeners.DateSetListener;
import ro.handrea.timelancer.views.listeners.ViewScrollListener;
import ro.handrea.timelancer.viewmodels.TimeLogsViewModel;
import ro.handrea.timelancer.views.activities.MainActivity;
import ro.handrea.timelancer.views.adapters.TimeLogsAdapter;

public class TimeLogsFragment extends FabAwareFragment implements DatePickerDialog.OnDateSetListener {
    public static final int NEW_TIME_LOG_REQUEST_CODE = 16;
    public static final String STARTING_DATE_KEY = "startingDateBundleKey";
    private static final String TAG = TimeLogsFragment.class.getSimpleName();

    private Calendar mCurrentSelectedDate;
    private DatePickerDialog mDatePickerDialog;
    private TimeLogsViewModel mViewModel;
    private TimeLogsAdapter mTimeLogsAdapter;

    public TimeLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_logs, container, false);
        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_time_logs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Executor executor = new ThreadPerTaskExecutor();
        mTimeLogsAdapter = new TimeLogsAdapter(executor);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mTimeLogsAdapter);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.equals(TimeLogsViewModel.class)) {
                    return (T) new TimeLogsViewModel(AppDatabase.getInstance(getContext()),
                            new ThreadPerTaskExecutor());
                }
                // Needed for NonNull annotation
                return (T) new Object();
            }
        }).get(TimeLogsViewModel.class);
        long startingDateMillis = getArguments().getLong(STARTING_DATE_KEY);
        mCurrentSelectedDate = Calendar.getInstance();
        mCurrentSelectedDate.setTimeInMillis(startingDateMillis);

        mViewModel.filterTimeLogsBy(mCurrentSelectedDate.getTime()).observe(
                TimeLogsFragment.this, timeLogs -> mTimeLogsAdapter.setData(timeLogs));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_date){
            showDatePickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        if (mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialog(getActivity(), this,
                    mCurrentSelectedDate.get(Calendar.YEAR),
                    mCurrentSelectedDate.get(Calendar.MONTH),
                    mCurrentSelectedDate.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCurrentSelectedDate.set(year, month, dayOfMonth);
        Date date = mCurrentSelectedDate.getTime();
        // This will trigger an update of the adapter
        mViewModel.filterTimeLogsBy(date);

        // We tell the parent activity that we have a new date in order to update the toolbar subtitle
        FragmentActivity activity = getActivity();

        if (activity instanceof DateSetListener) {
            ((DateSetListener) activity).onDateSet(date);
        }
    }

    @Override
    public void onFabClick() {
        NewTimeEntryActivity.startForResult(this, NEW_TIME_LOG_REQUEST_CODE, mCurrentSelectedDate.getTimeInMillis());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TIME_LOG_REQUEST_CODE && resultCode == SupportActivity.RESULT_OK) {
            TimeLog timeLog = data.getParcelableExtra(NewTimeEntryActivity.TIME_LOG_ENTRY);

            mViewModel.addTimeLog(timeLog);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
            mDatePickerDialog.dismiss();
        }
    }
}

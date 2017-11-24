package ro.handrea.timelancer.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.ViewScrollListener;
import ro.handrea.timelancer.views.MainActivity;
import ro.handrea.timelancer.views.TimeLogsAdapter;

public class TimeLogsFragment extends Fragment {
    public static final String STARTING_DATE_BUNDLE_KEY = "startingDateBundleKey";
    private static final String TAG = TimeLogsFragment.class.getSimpleName();

    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialog;

    public TimeLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long startingDateMillis = getArguments().getLong(STARTING_DATE_BUNDLE_KEY);
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(startingDateMillis);
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
        TimeLogsAdapter timeLogsAdapter = new TimeLogsAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(timeLogsAdapter);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
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
            mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO: Filter the time logs by the selected date

                    // We tell the parent activity that we have a new date in order to update the toolbar subtitle
                    FragmentActivity activity = getActivity();
                    if (activity instanceof DatePickerDialog.OnDateSetListener) {
                        ((DatePickerDialog.OnDateSetListener) activity).onDateSet(view, year, month, dayOfMonth);
                    }
                }
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
            mDatePickerDialog.dismiss();
        }
    }
}

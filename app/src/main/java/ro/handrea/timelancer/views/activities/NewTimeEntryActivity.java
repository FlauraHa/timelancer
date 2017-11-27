package ro.handrea.timelancer.views.activities;

import android.app.TimePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;
import ro.handrea.timelancer.R;
import ro.handrea.timelancer.ThreadPerTaskExecutor;
import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.models.TimeLog;
import ro.handrea.timelancer.models.WorkTime;
import ro.handrea.timelancer.viewmodels.NewTimeEntryViewModel;
import ro.handrea.timelancer.views.dialogs.HoursPickerDialog;
import ro.handrea.timelancer.views.fragments.TimeLogsFragment;
import ro.handrea.timelancer.views.listeners.TimeSetListener;

public class NewTimeEntryActivity extends AppCompatActivity implements TimeSetListener {
    public static final String TIME_LOG_ENTRY = "newTimeLog";

    private EditText mHours;
    private MaterialSpinner mProjectSpinner;
    private MaterialSpinner mActivitySpinner;
    private NewTimeEntryViewModel mViewModel;
    private HoursPickerDialog mHoursPickerDialog;
    private WorkTime mWorkTime;
    private Date mNewTimeEntryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time_entry);
        mViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.equals(NewTimeEntryViewModel.class)) {
                    return (T) new NewTimeEntryViewModel(
                            AppDatabase.getInstance(NewTimeEntryActivity.this),
                            new ThreadPerTaskExecutor());
                }
                // Needed for NonNull annotation
                return (T) new Object();
            }
        }).get(NewTimeEntryViewModel.class);
        initUi();
    }

    private void initUi() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNewTimeEntryDate = new Date(getIntent().getLongExtra(TimeLogsFragment.STARTING_DATE_KEY,
                System.currentTimeMillis()));
        EditText dateEditText = findViewById(R.id.edit_text_date);
        dateEditText.setText(new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
                .format(mNewTimeEntryDate));
        dateEditText.setEnabled(false);

        final ArrayAdapter<Project> projectAdapter = new ArrayAdapter<>(NewTimeEntryActivity.this,
                R.layout.spinner_item, R.id.text_view_spinner_item);
        mViewModel.getProjects().observe(this, projectAdapter::addAll);
        mProjectSpinner = findViewById(R.id.spinner_project);
        mProjectSpinner.setAdapter(projectAdapter);

        final ArrayAdapter<Activity> activityAdapter = new ArrayAdapter<>(NewTimeEntryActivity.this,
                R.layout.spinner_item, R.id.text_view_spinner_item);
        mViewModel.getActivities().observe(this, activityAdapter::addAll);
        mActivitySpinner = findViewById(R.id.spinner_activity);
        mActivitySpinner.setAdapter(activityAdapter);

        mHours = findViewById(R.id.edit_text_hours);
        mHours.setInputType(InputType.TYPE_NULL);
        mHours.setOnClickListener(v -> showHoursPickerDialog());
        mHours.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showHoursPickerDialog();
            }
        });
    }

    private void showHoursPickerDialog() {
        mHoursPickerDialog = new HoursPickerDialog(this, this);
        mHoursPickerDialog.show();
    }

    @Override
    public void onTimeSet(int hour, int minute) {
        mWorkTime = new WorkTime(hour, minute);
        mHours.setText(mWorkTime.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_time_entry_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
            case R.id.action_done:
                Project project = (Project) mProjectSpinner.getSelectedItem();
                Activity activity = (Activity) mActivitySpinner.getSelectedItem();

                LiveData<Long> workTimeId = mViewModel.addWorkTime(mWorkTime);
                workTimeId.observe(this, aLong -> {
                    Intent activityResultIntent = new Intent();

                    activityResultIntent.putExtra(TIME_LOG_ENTRY, new TimeLog(mNewTimeEntryDate, project.getId(), activity.getId(), aLong));
                    setResult(AppCompatActivity.RESULT_OK, activityResultIntent);
                    finish();
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void startForResult(android.support.v4.app.Fragment ctx, int requestCode, long dateMillis) {
        Intent activityIntent = new Intent(ctx.getActivity(), NewTimeEntryActivity.class);
        activityIntent.putExtra(TimeLogsFragment.STARTING_DATE_KEY, dateMillis);
        ctx.startActivityForResult(activityIntent, requestCode);
    }
}

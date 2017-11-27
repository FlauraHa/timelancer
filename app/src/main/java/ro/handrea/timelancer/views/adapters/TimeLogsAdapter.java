package ro.handrea.timelancer.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.database.AppDatabase;
import ro.handrea.timelancer.models.Activity;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.models.TimeLog;
import ro.handrea.timelancer.models.WorkTime;

/**
 * Created on 11/23/17.
 */

public class TimeLogsAdapter extends RecyclerView.Adapter<TimeLogsAdapter.TimeLogsAdapterViewHolder> {
    private static final String TAG = TimeLogsAdapter.class.getSimpleName();

    private List<TimeLog> mTimeLogs;
    private Executor mExecutor;

    public TimeLogsAdapter(Executor executor) {
        this.mExecutor = executor;
    }

    public void setData(List<TimeLog> data) {
        this.mTimeLogs = data;
        notifyDataSetChanged();
    }

    @Override
    public TimeLogsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_time_logs, parent, false);

        return new TimeLogsAdapterViewHolder(mExecutor, view);
    }

    @Override
    public void onBindViewHolder(TimeLogsAdapterViewHolder viewHolder, int position) {
        viewHolder.load(mTimeLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return mTimeLogs != null ? mTimeLogs.size() : 0;
    }

    public class TimeLogsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private Executor mExecutor;

        private TextView mProjectTextView;
        private TextView mActivityTextView;
        private TextView mHoursTextView;

        public TimeLogsAdapterViewHolder(Executor executor, View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mExecutor = executor;
            mProjectTextView = itemView.findViewById(R.id.text_view_project);
            mActivityTextView = itemView.findViewById(R.id.text_view_activity);
            mHoursTextView = itemView.findViewById(R.id.text_view_hours);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: Implement this
        }

        public void load(TimeLog timeLog) {
            AppDatabase db = AppDatabase.getInstance(mContext);
            mExecutor.execute(() -> {
                Handler mainHandler = new Handler(mContext.getMainLooper());
                Project project = Project.getFor(db, timeLog);
                Activity activity = Activity.getFor(db, timeLog);
                WorkTime workHours = WorkTime.getFor(db, timeLog);

                mainHandler.post(() -> {
                    mProjectTextView.setText(project.getName());
                    mActivityTextView.setText(activity.getName());
                    mHoursTextView.setText(workHours.toString());
                });
            });
        }

    }
}

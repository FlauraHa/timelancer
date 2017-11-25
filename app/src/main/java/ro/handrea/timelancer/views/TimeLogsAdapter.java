package ro.handrea.timelancer.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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

    public void setData(List<TimeLog> data) {
        this.mTimeLogs = data;
        notifyDataSetChanged();
    }

    @Override
    public TimeLogsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_time_logs, parent, false);

        return new TimeLogsAdapterViewHolder(view);
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

        private TextView mProjectImageView;
        private TextView mActivityImageView;
        private TextView mHoursImageView;

        public TimeLogsAdapterViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mProjectImageView = (TextView) itemView.findViewById(R.id.text_view_project);
            mActivityImageView = (TextView) itemView.findViewById(R.id.text_view_activity);
            mHoursImageView = (TextView) itemView.findViewById(R.id.text_view_hours);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: Implement this
        }

        public void load(TimeLog timeLog) {
            AppDatabase db = AppDatabase.getInstance(mContext);
            Project project = db.projectDao().findById(timeLog.getProjectId());
            Activity activity = db.activityDao().findById(timeLog.getActivityId());
            WorkTime workHours = db.workTimeDao().findById(timeLog.getWorkTimeId());

            mProjectImageView.setText(project.getName());
            mActivityImageView.setText(activity.getName());
            mHoursImageView.setText(workHours.toString());
        }

    }
}

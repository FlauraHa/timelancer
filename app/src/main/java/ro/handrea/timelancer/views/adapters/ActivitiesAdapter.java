package ro.handrea.timelancer.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.models.Activity;

/**
 * Created on 11/26/17.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivitiesAdapterViewHolder> {
    private static final String TAG = ActivitiesAdapter.class.getSimpleName();

    private List<Activity> mActivities;

    public void setData(List<Activity> data) {
        this.mActivities = data;
        notifyDataSetChanged();
    }

    @Override
    public ActivitiesAdapter.ActivitiesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_activities, parent, false);

        return new ActivitiesAdapter.ActivitiesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivitiesAdapter.ActivitiesAdapterViewHolder viewHolder, int position) {
        viewHolder.load(mActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return mActivities != null ? mActivities.size() : 0;
    }

    public class ActivitiesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mActivityNameTextView;

        ActivitiesAdapterViewHolder(View itemView) {
            super(itemView);
            mActivityNameTextView = itemView.findViewById(R.id.text_view_activity_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: Implement this
        }

        void load(Activity activity) {
            mActivityNameTextView.setText(activity.getName());
        }

    }
}

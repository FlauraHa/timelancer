package ro.handrea.timelancer.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.models.Project;

/**
 * Created on 11/25/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsAdapterViewHolder> {
    private static final String TAG = ProjectsAdapter.class.getSimpleName();

    private List<Project> mProjects;

    public void setData(List<Project> data) {
        this.mProjects = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public ProjectsAdapter.ProjectsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_projects, parent, false);

        return new ProjectsAdapter.ProjectsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.ProjectsAdapterViewHolder viewHolder, int position) {
        viewHolder.load(mProjects.get(position));
    }

    @Override
    public int getItemCount() {
        return mProjects != null ? mProjects.size() : 0;
    }

    public class ProjectsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mProjectNameTextView;

        ProjectsAdapterViewHolder(View itemView) {
            super(itemView);
            mProjectNameTextView = itemView.findViewById(R.id.text_view_project_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: Implement this
        }

        void load(Project project) {
            mProjectNameTextView.setText(project.getName());
        }

    }
}

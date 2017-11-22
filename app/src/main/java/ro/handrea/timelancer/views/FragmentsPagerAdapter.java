package ro.handrea.timelancer.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.views.fragments.ActivitiesFragment;
import ro.handrea.timelancer.views.fragments.ProjectsFragment;
import ro.handrea.timelancer.views.fragments.TimeLogsFragment;

class FragmentsPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> mFragments;

    public FragmentsPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        setupFragments(context);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    private void setupFragments(Context context) {
        Resources resources = context.getResources();
        int timeLogPosition = resources.getInteger(R.integer.nav_time_log_fragment_position);
        int projectsPosition = resources.getInteger(R.integer.nav_projects_fragment_position);
        int activitiesPosition = resources.getInteger(R.integer.nav_activities_fragment_position);
        mFragments = new SparseArray<>();
        mFragments.append(timeLogPosition, new TimeLogsFragment());
        mFragments.append(projectsPosition, new ProjectsFragment());
        mFragments.append(activitiesPosition, new ActivitiesFragment());
    }
}

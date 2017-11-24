package ro.handrea.timelancer.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.Date;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.views.fragments.ActivitiesFragment;
import ro.handrea.timelancer.views.fragments.ProjectsFragment;
import ro.handrea.timelancer.views.fragments.TimeLogsFragment;

class FragmentsPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> mFragments;

    FragmentsPagerAdapter(Context context, FragmentManager fragmentManager,
                          Date startingDate) {
        super(fragmentManager);
        setupFragments(context, startingDate);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    private void setupFragments(Context context, Date startingDate) {
        Resources resources = context.getResources();
        int timeLogsPosition = resources.getInteger(R.integer.nav_time_logs_fragment_position);
        int projectsPosition = resources.getInteger(R.integer.nav_projects_fragment_position);
        int activitiesPosition = resources.getInteger(R.integer.nav_activities_fragment_position);

        TimeLogsFragment timeLogsFragment = new TimeLogsFragment();
        Bundle args = new Bundle();
        args.putLong(TimeLogsFragment.STARTING_DATE_BUNDLE_KEY, startingDate.getTime());
        timeLogsFragment.setArguments(args);

        mFragments = new SparseArray<>();
        mFragments.append(timeLogsPosition, timeLogsFragment);
        mFragments.append(projectsPosition, new ProjectsFragment());
        mFragments.append(activitiesPosition, new ActivitiesFragment());
    }
}

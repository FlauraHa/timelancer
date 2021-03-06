package ro.handrea.timelancer.views.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.views.adapters.FragmentsPagerAdapter;
import ro.handrea.timelancer.views.fragments.FabAwareFragment;
import ro.handrea.timelancer.views.listeners.DateSetListener;
import ro.handrea.timelancer.views.listeners.ViewScrollListener;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ViewScrollListener, DateSetListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SELECTED_DATE_PREFS_KEY = "selectedDate";

    private int mCurrentFragmentPosition;
    private ViewPager mViewPager;
    private BottomNavigationView mNavigation;
    private FloatingActionButton mAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date startingDate = getDateFromPrefs();
        setDateSubtitle(startingDate);

        FragmentsPagerAdapter pagerAdapter =
                new FragmentsPagerAdapter(this, getSupportFragmentManager(), startingDate);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mNavigation = findViewById(R.id.bottom_navigation_view);
        mNavigation.setOnNavigationItemSelectedListener(this);

        mAddFab = findViewById(R.id.fab_add);
        mAddFab.setOnClickListener(this);
    }

    // This method is called when an item from the bottom navigation menu is clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mViewPager.setCurrentItem(item.getOrder());
        onFragmentChanged(item.getOrder());

        return true;
    }

    private void onFragmentChanged(int newFragmentPosition) {
        mCurrentFragmentPosition = newFragmentPosition;
        // Checking if we are moving to a different fragment than TimeLogs
        if (newFragmentPosition != getResources().getInteger(R.integer.nav_time_logs_fragment_position)) {
            removeDateSubtitle();
        } else {
            setDateSubtitle(getDateFromPrefs());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // No operation
    }

    // This method is called when a page/fragment is swiped
    @Override
    public void onPageSelected(int position) {
        mNavigation.getMenu().getItem(position).setChecked(true);
        onFragmentChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // No operation
    }

    // This is called when the mAddFab was clicked
    @Override
    public void onClick(View v) {
        FragmentsPagerAdapter adapter = (FragmentsPagerAdapter) mViewPager.getAdapter();
        FabAwareFragment fragment = (FabAwareFragment) adapter.getItem(mCurrentFragmentPosition);
        fragment.onFabClick();
    }

    // This method is called from a child fragment
    @Override
    public void onViewScrolled() {
        if (mAddFab != null && mAddFab.isShown()) {
            mAddFab.hide();
        }
    }

    // This method is called from a child fragment
    @Override
    public void onViewScrollStateIdle() {
        if (mAddFab != null) {
            mAddFab.show();
        }
    }

    // This method is called from a child fragment
    @Override
    public void onDateSet(Date date) {
        setDateSubtitle(date);
        saveDateToPrefs(date);
    }

    private void saveDateToPrefs(Date date) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(SELECTED_DATE_PREFS_KEY, date.getTime());
        editor.apply();
    }

    private Date getDateFromPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long dateMillis = preferences.getLong(SELECTED_DATE_PREFS_KEY, Calendar.getInstance().getTimeInMillis());

        return new Date(dateMillis);
    }

    private void setDateSubtitle(Date date) {
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            String pickedDate = new SimpleDateFormat(getString(R.string.date_format),
                    Locale.getDefault()).format(date);

            supportActionBar.setSubtitle(pickedDate);
        }
    }

    private void removeDateSubtitle() {
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setSubtitle("");
        }
    }
}

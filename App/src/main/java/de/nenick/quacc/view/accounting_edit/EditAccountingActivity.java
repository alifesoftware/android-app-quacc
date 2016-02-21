package de.nenick.quacc.view.accounting_edit;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import de.nenick.quacc.R;

@EActivity(R.layout.activity)
public class EditAccountingActivity extends ActionBarActivity {

    public static final String TAG_FRAGMENT = EditAccountingFragment.class.getSimpleName();

    @Extra
    long bookingEntryId;

    @AfterViews
    protected void onAfterViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT) == null) {
            fragmentManager.beginTransaction().add(R.id.container, EditAccountingFragment_.builder().bookingEntryId(bookingEntryId).build(), TAG_FRAGMENT).commit();
        }
    }
}

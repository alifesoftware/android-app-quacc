package de.nenick.quacc.apptest;

import android.app.Activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import de.nenick.quacc.R;
import de.nenick.quacc.view.main.MainActivity_;

/**
 * Emulates the android launcher for instrumentation tests.
 * <p/>
 * With this dummy we can write back press specifications without killing the app.
 * Killing the app abort the test and let it fail.
 * <p/>
 * Note: Found no way to locate it under androidTest sources.
 */
@EActivity(R.layout.activity_dummy_launcher)
public class DummyLauncherActivity extends Activity {

    @Click(R.id.btn_start_app)
    protected void onClick() {
        MainActivity_.intent(this).start();
    }
}

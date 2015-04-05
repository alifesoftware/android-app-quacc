package de.nenick.quacc.database.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.nenick.quacc.database.BuildConfig;
import de.nenick.quacc.database.initialdata.DatabaseInitialData;

/**
 * Implement your custom database creation or upgrade code here.
 * <p/>
 * This file will not be overwritten if you re-run the content provider generator.
 */
public class QuAccSQLiteOpenHelperCallbacks {
    private static final String TAG = QuAccSQLiteOpenHelperCallbacks.class.getSimpleName();

    public void onOpen(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOpen");
        // Insert your db open code here.
    }

    public void onPreCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPreCreate");
        // Insert your db creation code here. This is called before your tables are created.
    }

    public void onPostCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPostCreate");
        // Insert your db creation code here. This is called after your tables are created.
        new DatabaseInitialData().insert(db);
    }

    public void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Insert your upgrading code here.
    }
}

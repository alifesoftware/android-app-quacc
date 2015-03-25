package de.nenick.quacc.database.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import de.nenick.quacc.database.BuildConfig;
import de.nenick.quacc.database.provider.account.AccountColumns;
import de.nenick.quacc.database.provider.accounting.AccountingColumns;
import de.nenick.quacc.database.provider.accountingcategory.AccountingCategoryColumns;
import de.nenick.quacc.database.provider.accountinginterval.AccountingIntervalColumns;

public class QuAccSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = QuAccSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "quacc.db";
    private static final int DATABASE_VERSION = 1;
    private static QuAccSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final QuAccSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS "
            + AccountColumns.TABLE_NAME + " ( "
            + AccountColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountColumns.NAME + " TEXT NOT NULL, "
            + AccountColumns.DESCRIPTION + " TEXT "
            + ", CONSTRAINT unique_name UNIQUE (account__name) ON CONFLICT FAIL"
            + " );";

    public static final String SQL_CREATE_TABLE_ACCOUNTING = "CREATE TABLE IF NOT EXISTS "
            + AccountingColumns.TABLE_NAME + " ( "
            + AccountingColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountingColumns.ACCOUNT_ID + " INTEGER NOT NULL, "
            + AccountingColumns.NAME + " TEXT NOT NULL, "
            + AccountingColumns.DESCRIPTION + " TEXT NOT NULL, "
            + AccountingColumns.ACCOUNTING_INTERVAL_ID + " INTEGER NOT NULL, "
            + AccountingColumns.ACCOUNTING_CATEGORY_ID + " INTEGER NOT NULL, "
            + AccountingColumns.ACCOUNTING_DATE + " INTEGER NOT NULL, "
            + AccountingColumns.ACCOUNTING_TYPE + " INTEGER NOT NULL, "
            + AccountingColumns.VALUE + " INTEGER NOT NULL "
            + ", CONSTRAINT fk_account_id FOREIGN KEY (" + AccountingColumns.ACCOUNT_ID + ") REFERENCES account (_id) ON DELETE CASCADE"
            + ", CONSTRAINT fk_accounting_interval_id FOREIGN KEY (" + AccountingColumns.ACCOUNTING_INTERVAL_ID + ") REFERENCES accounting_interval (_id) ON DELETE CASCADE"
            + ", CONSTRAINT fk_accounting_category_id FOREIGN KEY (" + AccountingColumns.ACCOUNTING_CATEGORY_ID + ") REFERENCES accounting_category (_id) ON DELETE CASCADE"
            + " );";

    public static final String SQL_CREATE_TABLE_ACCOUNTING_CATEGORY = "CREATE TABLE IF NOT EXISTS "
            + AccountingCategoryColumns.TABLE_NAME + " ( "
            + AccountingCategoryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountingCategoryColumns.NAME + " TEXT NOT NULL "
            + ", CONSTRAINT unique_name UNIQUE (accounting_category__name) ON CONFLICT FAIL"
            + " );";

    public static final String SQL_CREATE_TABLE_ACCOUNTING_INTERVAL = "CREATE TABLE IF NOT EXISTS "
            + AccountingIntervalColumns.TABLE_NAME + " ( "
            + AccountingIntervalColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountingIntervalColumns.NAME + " TEXT NOT NULL "
            + ", CONSTRAINT unique_name UNIQUE (accounting_interval__name) ON CONFLICT FAIL"
            + " );";

    // @formatter:on

    public static QuAccSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static QuAccSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static QuAccSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new QuAccSQLiteOpenHelper(context);
    }

    private QuAccSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new QuAccSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static QuAccSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new QuAccSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private QuAccSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new QuAccSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_ACCOUNT);
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTING);
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTING_CATEGORY);
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTING_INTERVAL);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
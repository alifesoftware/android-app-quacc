package de.nenick.quacc.database.provider.account;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.nenick.quacc.database.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code account} table.
 */
public class AccountContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return AccountColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable AccountSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * The name of the account.
     */
    public AccountContentValues putName(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("name must not be null");
        mContentValues.put(AccountColumns.NAME, value);
        return this;
    }


    /**
     * The base to calculate the current amount of money. Values are stored in cents.
     */
    public AccountContentValues putInitialvalue(int value) {
        mContentValues.put(AccountColumns.INITIALVALUE, value);
        return this;
    }

}

package de.nenick.quacc.database.provider.accountingtemplate;

import de.nenick.quacc.database.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code accounting_template} table.
 */
public interface AccountingTemplateModel extends BaseModel {

    /**
     * Get the {@code account_id} value.
     */
    long getAccountId();

    /**
     * Get the {@code category_id} value.
     */
    long getCategoryId();

    /**
     * Get the {@code comment} value.
     * Can be {@code null}.
     */
    @Nullable
    String getComment();

    /**
     * Get the {@code interval} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getInterval();

    /**
     * Get the {@code type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getType();

    /**
     * Values are stored in 100 cent.
     */
    int getValue();
}

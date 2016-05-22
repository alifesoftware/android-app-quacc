package de.nenick.quacc.view.bookingentries;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.nenick.quacc.R;
import de.nenick.toolscollection.lazyadapter.LazyAdapter;

@EViewGroup(R.layout.fragment_booking_entries)
class BookingEntriesView extends FrameLayout {

    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    @ViewById(R.id.bookingDateSelectorLayout)
    protected View dateSelector;

    public BookingEntriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        LazyAdapter.inject(recyclerView, adapter);
    }

    public void toggleDateSelector() {
        dateSelector.setVisibility(dateSelector.getVisibility() == VISIBLE ? GONE : VISIBLE);
    }
}

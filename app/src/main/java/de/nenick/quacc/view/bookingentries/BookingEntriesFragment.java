package de.nenick.quacc.view.bookingentries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.joda.time.DateTime;

import de.nenick.quacc.R;

@EFragment
@OptionsMenu(R.menu.date_selector)
public class BookingEntriesFragment extends Fragment {

    BookingEntriesView view;

    @Bean
    BookingEntriesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = BookingEntriesView_.build(inflater.getContext(), null);
    }

    @AfterViews
    void onAfterViewsCreated() {
        view.setAdapter(adapter);
    }

    public void setAccount(long account) {
        adapter.update(account, new DateTime(0), new DateTime());
    }

    @OptionsItem(R.id.dateSelectorToggle)
    public void onClickDateSelectorToggle() {
        view.toggleDateSelector();
    }

}

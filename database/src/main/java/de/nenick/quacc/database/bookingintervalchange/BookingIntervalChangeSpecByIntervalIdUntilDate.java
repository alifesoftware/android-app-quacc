package de.nenick.quacc.database.bookingintervalchange;

import java.util.Date;

import de.nenick.quacc.database.provider.bookingintervalchange.BookingIntervalChangeSelection;

public class BookingIntervalChangeSpecByIntervalIdUntilDate extends BookingIntervalChangeSpec {

    private long intervalId;
    private Date date;

    public BookingIntervalChangeSpecByIntervalIdUntilDate(long intervalId, Date date) {
        this.intervalId = intervalId;
        this.date = date;
    }

    @Override
    public BookingIntervalChangeSelection toSelection() {
        return new BookingIntervalChangeSelection()
                .bookingIntervalId(intervalId)
                .and().dateBeforeEq(date);
    }
}

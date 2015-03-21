package de.nenick.quacc.main;

import org.junit.Test;

import de.nenick.quacc.accountinglist.RoboAccountingListPage;
import de.nenick.robolectric.RoboSup;
import de.nenick.robolectric.RobolectricSupportedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MainFragmentTest extends RobolectricSupportedTest {

    RoboSup<MainActivity_, MainFragment_> robo = new RoboSup<>();
    RoboMainPage mainPage = new RoboMainPage(robo);

    @Test
    public void shouldStartNextPage() {
        mainPage.startPage();
        assertThat(mainPage.nextStartedPage()).isEqualTo(RoboAccountingListPage.Intent());
    }
}
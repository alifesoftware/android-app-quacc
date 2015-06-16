package de.nenick.quacc.view.accounting_create;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowSQLiteConnection;

import de.nenick.quacc.R;
import de.nenick.quacc.common.util.QuAccDateUtil;
import de.nenick.quacc.database.AccountingDb;
import de.nenick.quacc.database.AccountingDb_;
import de.nenick.quacc.database.AccountingInterval;
import de.nenick.quacc.database.AccountingType;
import de.nenick.quacc.database.provider.accounting.AccountingCursor;
import de.nenick.robolectric.RoboComponentTestBase;
import de.nenick.robolectric.RoboSup;

import static de.nenick.quacc.test.TestDateUtil.day;
import static de.nenick.quacc.test.TestDateUtil.month;
import static de.nenick.quacc.test.TestDateUtil.year;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateAccountingSpec extends RoboComponentTestBase {

    RoboSup<CreateAccountingActivity_, CreateAccountingFragment> robo = new RoboSup<>();
    RoboCreateAccountingPage addAccountingPage = new RoboCreateAccountingPage(robo);
    AccountingDb accountingDb;

    final String defaultDate = "21.12.2012";
    private Application context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        accountingDb = AccountingDb_.getInstance_(context);
    }

    @Test
    public void shouldShowValueFromDatePicker() {
       addAccountingPage.startPage();
       whenPickDate(defaultDate);
       assertThat(addAccountingPage.dateField().getText()).isEqualTo(defaultDate);
    }

    @Test
    public void shouldSaveStateForConfigChanges() {
        addAccountingPage.startPage();

        addAccountingPage.accountSpinner().entry("Bar").select();
        addAccountingPage.typeSpinner().entry("Einnahme").select();
        addAccountingPage.intervalSpinner().entry("Monatlich").select();
        addAccountingPage.categorySpinner().entry("eBay").select();
        whenPickDate(defaultDate);
        addAccountingPage.valueField().setText("60.00");

        robo.activityController.restart();

        assertThat(addAccountingPage.accountSpinner().selectedEntry().getText()).isEqualTo("Bar");
        assertThat(addAccountingPage.typeSpinner().selectedEntry().getText()).isEqualTo("Einnahme");
        assertThat(addAccountingPage.intervalSpinner().selectedEntry().getText()).isEqualTo("Monatlich");
        assertThat(addAccountingPage.categorySpinner().selectedEntry().getText(R.id.text1)).isEqualTo("eBay");
        assertThat(addAccountingPage.dateField().getText()).isEqualTo(defaultDate);
        assertThat(addAccountingPage.valueField().getText()).isEqualTo("60.00");
    }

    @Test
    public void shouldAddAccounting()  {
        addAccountingPage.startPage();
        addAccountingPage.valueField().setText("60,00");
        addAccountingPage.actionbar().confirmMenuItem().click();
        assertThat(AccountingDb_.getInstance_(context).getAll().getCount()).isEqualTo(1);
    }

    @Test
    public void shouldAddNewAccountingWithGivenInputValues() {
        addAccountingPage.startPage();
        assertThat(accountingDb.getAll().getCount()).isZero();

        addAccountingPage.accountSpinner().entry("Bar").select();
        addAccountingPage.intervalSpinner().entry("Einmahlig").select();
        addAccountingPage.typeSpinner().entry("Einnahme").select();
        addAccountingPage.categorySpinner().entry("eBay").select();
        whenPickDate(defaultDate);
        addAccountingPage.valueField().setText("5,83");
        addAccountingPage.commentField().setText("take the money");

        addAccountingPage.actionbar().confirmMenuItem().click();

        AccountingCursor accountings = accountingDb.getAll();
        assertThat(accountings.getCount()).isEqualTo(1);
        accountings.moveToFirst();
        assertThat(accountings.getAccountName()).isEqualTo("Bar");
        assertThat(accountings.getInterval()).isEqualTo(AccountingInterval.once.name());
        assertThat(accountings.getType()).isEqualTo(AccountingType.incoming.name());
        assertThat(accountings.getCategoryName()).isEqualTo("eBay");
        assertThat(accountings.getDate()).isEqualTo(QuAccDateUtil.toDate(defaultDate));
        assertThat(accountings.getValue()).isEqualTo(583);
        assertThat(accountings.getComment()).isEqualTo("take the money");
    }

    @Test
    public void shouldGiveFeedbackForInvalidValues() {
        ShadowSQLiteConnection.reset();
        addAccountingPage.startPage();

        whenValueIs("A");
        thenHintIsShown("Zugelassene zeichen sind 0 bis 9 und \",\"");

        whenValueIs("30,30,30");
        thenHintIsShown("Nutze als Format z.B. 4500,00");

        whenValueIs("");
        thenHintIsShown("Gebe noch einen Betrag an");

        whenValueIs("0");
        thenHintIsShown("Gebe noch einen Betrag an");
    }

    private void whenPickDate(String date) {
        addAccountingPage.dateField().click();
        assertThat(addAccountingPage.dialog().datePicker().isShowing()).isTrue();

        // since updateOpenCloseState from robolectric 3.0-rc2 to rc3 picking the date result in long running tests
        // the issue is tracked at https://github.com/robolectric/robolectric/issues/1838
        //addAccountingPage.dialog().datePicker().pickDate(21, 12, 2012);

        addAccountingPage.dialog().datePicker().clickOk();

        // instead of picking the date whe set it direct until the issue is fixed
        robo.fragment.view.setDate(date);
    }

    private void thenHintIsShown(String noValidNumber) {
        assertThat(addAccountingPage.valueErrorField().getText()).contains(noValidNumber);
        accountingDb = AccountingDb_.getInstance_(context);
        assertThat(accountingDb.getAll().getCount()).isEqualTo(0);
    }

    private void whenValueIs(String value) {
        addAccountingPage.valueField().setText(value);
        addAccountingPage.actionbar().confirmMenuItem().click();
    }
}
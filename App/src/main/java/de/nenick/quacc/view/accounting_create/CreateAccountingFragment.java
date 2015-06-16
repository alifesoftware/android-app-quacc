package de.nenick.quacc.view.accounting_create;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import java.util.Date;

import de.nenick.quacc.R;
import de.nenick.quacc.accounting.creation.CreateAccountingFunction;
import de.nenick.quacc.accounting.creation.CreateIntervalFunction;
import de.nenick.quacc.view.category.GetAccountingCategoriesFilteredFunction;
import de.nenick.quacc.accounting.GetAccountingIntervalsFunction;
import de.nenick.quacc.accounting.GetAccountingTypesFunction;
import de.nenick.quacc.account.GetAccountsFunction;
import de.nenick.quacc.common.valueparser.ParseValueFromStringFunction;
import de.nenick.quacc.common.mvp.BasePresenterFragment;
import de.nenick.quacc.common.mvp.BaseView;
import de.nenick.quacc.common.util.QuAccDateUtil;
import de.nenick.quacc.database.AccountingInterval;
import de.nenick.quacc.database.AccountingType;
import de.nenick.quacc.view.accounting_create.adapter.CategoryAdapter;
import de.nenick.quacc.view.accounting_create.adapter.IntervalAdapter;
import de.nenick.quacc.view.accounting_create.adapter.TypeAdapter;

import static de.nenick.quacc.common.valueparser.ParseValueFromStringFunction.ParseResult.Successful;

@EFragment(R.layout.fragment_create_accounting)
@OptionsMenu(R.menu.menu_create_account)
public class CreateAccountingFragment extends BasePresenterFragment {

    @Bean
    CreateAccountingView view;

    @Bean
    GetAccountingCategoriesFilteredFunction getAccountingCategoriesFilteredFunction;

    @Bean
    GetAccountingIntervalsFunction getAccountingIntervalsFunction;

    @Bean
    GetAccountingTypesFunction getAccountingTypesFunction;

    @Bean
    GetAccountsFunction getAccountsFunction;

    @Bean
    CreateAccountingFunction createAccountingFunction;

    @Bean
    CreateIntervalFunction createIntervalFunction;

    @Bean
    ParseValueFromStringFunction parseValueFromStringFunction;

    @Bean
    SpeechRecognitionFeature speechRecognitionFeature;

    @Bean
    IntervalAdapter intervalAdapter;

    @Bean
    TypeAdapter typeAdapter;

    @Bean
    CategoryAdapter categoryAdapter;

    @Override
    protected BaseView getBaseView() {
        return view;
    }

    @Override
    protected void onViewStart() {
        view.setDate(QuAccDateUtil.currentDate());
        view.setEndDate(QuAccDateUtil.currentDate());
        view.hideEndDate();

        view.setAccounts(getAccountsFunction.apply());
        speechRecognitionFeature.setView(view);

        intervalAdapter.addAll(getAccountingIntervalsFunction.apply());
        view.setAccountingIntervals(intervalAdapter);
        view.setAccountingInterval(AccountingInterval.once.name());

        typeAdapter.addAll(getAccountingTypesFunction.apply());
        view.setAccountingTypes(typeAdapter);
        view.setAccountingType(AccountingType.outgoing.name());

        view.setAccountingCategories(categoryAdapter);
        reloadCategories();

    }

    @Override
    protected void onViewPause() {
        speechRecognitionFeature.onViewPause();
    }

    @Override
    protected void onViewFinish() {
        speechRecognitionFeature.onViewFinish();
    }

    @OptionsItem(R.id.confirm)
    protected void onConfirmNewAccounting() {
        view.closeSoftKeyboard();
        String value = view.getValue();
        ParseValueFromStringFunction.Result valueResult = parseValueFromStringFunction.apply(value);
        if (valueResult.report == Successful) {
            String account = view.getAccount();
            String accountingType = view.getAccountingType();
            String accountingInterval = view.getAccountingInterval();
            String accountingCategory = view.getAccountingCategory();
            String dateString = view.getDate();
            String comment = view.getComment();
            view.finish();
            Date date = QuAccDateUtil.toDate(dateString);
            if(accountingInterval.equals(AccountingInterval.once.name())) {
                createAccountingFunction.apply(account, accountingType, accountingInterval, accountingCategory, date, valueResult.value, comment);
            } else {
                if(view.isEndDateActive()) {
                    String endDateString = view.getEndDate();
                    Date endDate = QuAccDateUtil.toDate(endDateString);
                    createIntervalFunction.applyWithEndDate(account, accountingType, accountingInterval, accountingCategory, date, endDate
                            , valueResult.value, comment);
                } else {
                    createIntervalFunction.apply(account, accountingType, accountingInterval, accountingCategory, date, valueResult.value, comment);
                }
            }
        } else {
            showParsingError(valueResult);
        }
    }

    private void showParsingError(ParseValueFromStringFunction.Result valueResult) {
        switch (valueResult.report) {
            case EmptyInput:
                view.showValueParsingError(R.string.parse_error_missing_value);
                break;
            case ZeroValue:
                view.showValueParsingError(R.string.parse_error_missing_value);
                break;
            case InvalidChar:
                view.showValueParsingError(R.string.parse_error_invalid_char);
                break;
            case InvalidFormat:
                view.showValueParsingError(R.string.parse_error_invalid_format);
                break;
            case UnknownError:
            default:
                view.showValueParsingError(R.string.parse_error_unknown);
        }
    }

    @CheckedChange(R.id.endDateCheck)
    public void onToggleEndDate() {
        if(view.isEndDateActive()) {
            view.enableEndDate();
        } else {
            view.disableEndDate();
        }
    }

    @ItemSelect(R.id.interval)
    public void onIntervalSelection(boolean selected, int position) {
        if(view.getAccountingInterval().equals(AccountingInterval.once.name())) {
            view.hideEndDate();
        } else {
            view.showEndDate();
        }
        reloadCategories();
    }

    @ItemSelect(R.id.type)
    public void onTypeSelection(boolean selected, int position) {
        reloadCategories();
    }

    private void reloadCategories() {
        String accountingInterval = view.getAccountingInterval();
        String accountingType = view.getAccountingType();
        if (isViewNotFullInitialized(accountingInterval, accountingType)) {
            return;
        }
        categoryAdapter.updateFor(accountingInterval, accountingType);
        //view.setAccountingCategories(getAccountingCategoriesFunction.apply(accountingInterval, accountingType));
    }

    private boolean isViewNotFullInitialized(String accountingInterval, String accountingType) {
        return accountingInterval == null || accountingType == null;
    }
}
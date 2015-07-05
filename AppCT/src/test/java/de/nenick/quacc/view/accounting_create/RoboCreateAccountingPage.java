package de.nenick.quacc.view.accounting_create;

import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collections;

import de.nenick.quacc.R;
import de.nenick.quacc.speechrecognition.QuAccSpeechRecognizerListener;
import de.nenick.robolectric.RoboSup;
import de.nenick.robolectric.RoboSupPage;
import de.nenick.robolectricpages.components.RoboImageButton;
import de.nenick.robolectricpages.components.RoboSpinner;
import de.nenick.robolectricpages.components.RoboTextView;

import static org.mockito.Mockito.verify;

public class RoboCreateAccountingPage extends RoboSupPage<CreateAccountingActivity_, CreateAccountingFragment> {

    @Captor
    ArgumentCaptor<QuAccSpeechRecognizerListener> speechRecognitionListenerArgumentCaptor;

    public RoboCreateAccountingPage() {
        super(new RoboSup<CreateAccountingActivity_, CreateAccountingFragment>(), CreateAccountingActivity.TAG_FRAGMENT);
        MockitoAnnotations.initMocks(this);
    }

    public static Intent Intent() {
        return CreateAccountingActivity_.intent(RuntimeEnvironment.application).get();
    }

    public void startPageWithSpeechMock(SpeechRecognizer mockSpeechRecognizer) {
        createPage();
        RoboCreateAccountingFragmentMockUtil.setSpeechRecognitionMock(robo.fragment, mockSpeechRecognizer);
        verify(mockSpeechRecognizer).setRecognitionListener(speechRecognitionListenerArgumentCaptor.capture());
        startCreatedPage();
    }

    public RoboSpinner typeSpinner() {
        return new RoboSpinner(robo, R.id.type);
    }

    public RoboTextView dateField() {
        return new RoboTextView(robo, R.id.date);
    }

    public RoboSpinner intervalSpinner() {
        return new RoboSpinner(robo, R.id.interval);
    }

    public RoboCategorySpinner categorySpinner() {
        return new RoboCategorySpinner(robo, R.id.category);
    }

    public RoboSpinner accountSpinner() {
        return new RoboSpinner(robo, R.id.account);
    }

    public RoboTextView speechResultField() {
        return new RoboTextView(robo, R.id.speechResult);
    }

    public RoboImageButton speechButton() {
        return new RoboImageButton(robo, R.id.btn_speech_recognition);
    }

    public void speechResult(String... texts) {
        Bundle bundle = new Bundle();
        ArrayList<String> strings = new ArrayList<>();
        Collections.addAll(strings, texts);
        bundle.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, strings);
        speechRecognitionListenerArgumentCaptor.getValue().onResults(bundle);
    }

    public RoboCreateAccountingDialogs dialog() {
        return new RoboCreateAccountingDialogs();
    }

    public void speechError(int error) {
        speechRecognitionListenerArgumentCaptor.getValue().onError(error);
    }

    public RoboCreateAccountingActionbar actionbar() {
        return new RoboCreateAccountingActionbar(robo);
    }

    public RoboTextView valueField() {
        return new RoboTextView(robo, R.id.value);
    }

    public RoboTextView commentField() {
        return new RoboTextView(robo, R.id.comment);
    }

    public RoboTextView valueErrorField() {
        return new RoboTextView(robo, R.id.valueError);
    }

    public RoboTextView endDateField() {
        return new RoboTextView(robo, R.id.endDate);
    }
}

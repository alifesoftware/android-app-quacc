package de.nenick.quacc.core.speechinterpreter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

public class RecognizeCategoryFunctionTest {
    @Test
    public void work() {
        fail("not ready");
    }
/*
    @Mock
    CategoryDb categoryDb;

    @Mock
    CategoryCursor categoryCursor;

    @InjectMocks
    RecognizeCategoryFunction recognizeCategoryFunction;

    SpeechResult speechResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindMatch() {
        given(categoryDb.getAll()).willReturn(categoryCursor);
        given(categoryCursor.moveToNext()).willReturn(true).willReturn(true).willReturn(false);
        given(categoryCursor.getName()).willReturn("Versicherung").willReturn("Miete");

        whenFindCategoryFor("Some text Versicherung Some text");

        assertThat(speechResult.value).isEqualTo("Versicherung");
    }

    @Test
    public void shouldFindBestMatch() {
        given(categoryDb.getAll()).willReturn(categoryCursor);
        given(categoryCursor.moveToNext()).willReturn(true).willReturn(true).willReturn(false);
        given(categoryCursor.getName()).willReturn("Versicherung").willReturn("Lebensversicherung");

        whenFindCategoryFor("Some text Lebensversicherung Some text");

        assertThat(speechResult.value).isEqualTo("Lebensversicherung");
    }

    private void whenFindCategoryFor(String text) {
        speechResult = recognizeCategoryFunction.apply(text);
    }
*/
}
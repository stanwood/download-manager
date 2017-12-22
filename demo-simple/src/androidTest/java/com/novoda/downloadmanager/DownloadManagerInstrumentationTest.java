package com.novoda.downloadmanager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.downloadmanager.demo.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DownloadManagerInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testActivityNotNull() {
        if (activityRule.getActivity() == null) {
            throw new AssertionError("activity null");
        }
    }

    @Test
    public void testCreatesV1Database() {
        onView(withId(R.id.button_create_v1_db))
                .perform(click())
                .check(matches(isDisplayed()));
    }
}

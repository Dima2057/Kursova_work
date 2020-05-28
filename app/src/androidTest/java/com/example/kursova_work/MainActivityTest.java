package com.example.kursova_work;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkDisplayedViews() {
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button2))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onClickSignUpButton() {
        onView(withId(R.id.button)).perform(click());
        onView(ViewMatchers.withId(R.id.editText6))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.button7))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button))
                .check(doesNotExist());
        onView(withId(R.id.button2))
                .check(doesNotExist());
    }

    @Test
    public void onClickSignInButton() {
        onView(withId(R.id.button2)).perform(click());
        onView(ViewMatchers.withId(R.id.editText))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.editText3))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.button4))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button))
                .check(doesNotExist());
        onView(withId(R.id.button2))
                .check(doesNotExist());
    }
}
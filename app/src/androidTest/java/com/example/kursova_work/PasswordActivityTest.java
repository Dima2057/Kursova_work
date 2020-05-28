package com.example.kursova_work;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class PasswordActivityTest {

    @Rule
    public ActivityTestRule<PasswordActivity> activityTestRule =
            new ActivityTestRule<>(PasswordActivity.class);

    @Test
    public void checkDisplayedViews() {
        onView(ViewMatchers.withId(R.id.editText2))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.button3))
                .check(matches(isDisplayed()));
    }
}
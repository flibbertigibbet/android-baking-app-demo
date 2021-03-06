package com.banderkat.recipes;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.banderkat.recipes.activities.RecipesMainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipesMainActivityTest {
    @Rule
    public ActivityTestRule<RecipesMainActivity> mActivityRule = new ActivityTestRule<>(
            RecipesMainActivity.class);

    /*
    @Before
    public void init(){
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    */

    @Test
    public void mainActivityShouldDisplay() {
        Espresso.onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }
}

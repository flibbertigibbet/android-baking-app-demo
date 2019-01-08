package com.banderkat.recipes.activities;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.banderkat.recipes.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BasicRecyclerViewTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipesMainActivity> mActivityTestRule = new ActivityTestRule<>(RecipesMainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = mActivityTestRule.getActivity().getCountingIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void basicRecyclerViewTest() {

        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_list_item_name), withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_list_item_card),
                                        childAtPosition(
                                                withId(R.id.recipe_list_recycler_view),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

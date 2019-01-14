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
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesEspressoTest {

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
    public void recipesEspressoTest() {
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

        ViewInteraction textView2 = onView(
                allOf(withText("Baking Time"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Baking Time")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.recipe_list_item_name), withText("Cheesecake"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_list_item_card),
                                        childAtPosition(
                                                withId(R.id.recipe_list_recycler_view),
                                                3)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Cheesecake")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list_recycler_view),
                        childAtPosition(
                                withId(R.id.recipe_list_fragment_layout),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.step_detail_item_label), withText("Step #3: Start water bath."),
                        childAtPosition(
                                allOf(withId(R.id.step_detail_card),
                                        childAtPosition(
                                                withId(R.id.recipe_step_recycler_view),
                                                3)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Step #3: Start water bath.")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.step_detail_item_label), withText("Step #4: Prebake cookie crust. "),
                        childAtPosition(
                                allOf(withId(R.id.step_detail_card),
                                        childAtPosition(
                                                withId(R.id.recipe_step_recycler_view),
                                                4)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Step #4: Prebake cookie crust. ")));
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

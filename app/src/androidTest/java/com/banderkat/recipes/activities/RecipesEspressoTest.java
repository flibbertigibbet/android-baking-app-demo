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

        ////////////////////

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withText("Cheesecake"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Cheesecake")));

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

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_step_recycler_view),
                        childAtPosition(
                                withId(R.id.recipe_step_fragment_layout),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.step_detail_next_btn),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                3),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.step_detail_prev_btn),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                2),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.step_detail_item_label), withText("Start water bath."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("Start water bath.")));

        ViewInteraction textView8 = onView(
                allOf(withText("Cheesecake"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText("Cheesecake")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.step_detail_next_btn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.step_detail_item_label), withText("Prebake cookie crust. "),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView9.check(matches(withText("Prebake cookie crust. ")));

        pressBack();

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.step_detail_item_label), withText("Start water bath."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView10.check(matches(withText("Start water bath.")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.recipe_list_item_name), withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_list_item_card),
                                        childAtPosition(
                                                withId(R.id.recipe_list_recycler_view),
                                                0)),
                                0),
                        isDisplayed()));
        textView11.check(matches(withText("Nutella Pie")));
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

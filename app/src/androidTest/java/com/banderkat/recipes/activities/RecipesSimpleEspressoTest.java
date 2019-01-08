package com.banderkat.recipes.activities;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.banderkat.recipes.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesSimpleEspressoTest {

    @Rule
    public ActivityTestRule<RecipesMainActivity> mActivityTestRule = new ActivityTestRule<>(RecipesMainActivity.class);

    @Test
    public void recipesSimpleEspressoTest() {
        ViewInteraction textView = onView(
allOf(withText("Baking Time"),
childAtPosition(
allOf(withId(R.id.action_bar),
childAtPosition(
withId(R.id.action_bar_container),
0)),
0),
isDisplayed()));
        textView.check(matches(withText("Baking Time")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.recipe_list_item_name), withText("Nutella Pie"),
childAtPosition(
allOf(withId(R.id.recipe_list_item_card),
childAtPosition(
withId(R.id.recipe_list_recycler_view),
0)),
0),
isDisplayed()));
        textView2.check(matches(withText("Nutella Pie")));
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.recipe_list_recycler_view),
childAtPosition(
withId(R.id.recipe_list_fragment_layout),
1)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.recipe_list_item_name), withText("Nutella Pie"),
childAtPosition(
allOf(withId(R.id.recipe_list_item_card),
childAtPosition(
withId(R.id.recipe_list_recycler_view),
0)),
0),
isDisplayed()));
        textView3.check(matches(withText("Nutella Pie")));
        
        ViewInteraction textView4 = onView(
allOf(withId(R.id.recipe_list_item_name), withText("Cheesecake"),
childAtPosition(
allOf(withId(R.id.recipe_list_item_card),
childAtPosition(
withId(R.id.recipe_list_recycler_view),
3)),
0),
isDisplayed()));
        textView4.check(matches(withText("Cheesecake")));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }

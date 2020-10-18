package org.hitlabnz.sensor_fusion_demo.androidTest;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.hitlabnz.sensor_fusion_demo.R;
import org.hitlabnz.sensor_fusion_demo.SensorSelectionActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

///**
// * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
// */
//public class ApplicationTest extends ApplicationTestCase<Application> {
//    public ApplicationTest() {
//        super(Application.class);
//    }
//}


public class ApplicationTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            SensorSelectionActivity.class);

    @Test
    public void checkAssertMenuExists(){
        DiscardGyroscopeWarning();

        // Act & Assert
        AssertThatAboutActionMenuExists();
    }

    @Test
    public void clickAboutMenu_expectAboutToBeDisplayed(){
        DiscardGyroscopeWarning();

        // Act
        onView(withId(R.id.action_about)).perform(click());

        onView(withId(R.id.webViewAbout)).check(matches(ViewMatchers.isDisplayed()));
    }


    public void AssertThatAboutActionMenuExists() {
        onView(withId(R.id.action_about)).check(matches(withText("About")));
    }

    public static void DiscardGyroscopeWarning() {
        try {
            onView(withText("OK")).perform(click());
        } catch (NoMatchingViewException e) {
            //view not displayed logic
        }
    }
}
package net.paddlefish.lemonadestand;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by arahn on 12/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class StartGameTest {
	private void rotateScreen() {
		Context context = InstrumentationRegistry.getTargetContext();
		int orientation
				= context.getResources().getConfiguration().orientation;

		Activity activity = mMainActivityTestRule.getActivity();
		activity.setRequestedOrientation(
				(orientation == Configuration.ORIENTATION_PORTRAIT) ?
						ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
						ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Rule
	public ActivityTestRule<MainActivity> mMainActivityTestRule =
			new ActivityTestRule<MainActivity>(MainActivity.class);

	@Test
	public void clickPlayButton_showsMoneyScreen() throws Exception {
		onView(withId(R.id.startGameButton))
				.perform(click());
		onView(withId(R.id.bigDollarSign))
				.check(matches(isDisplayed()));
		rotateScreen();
		onView(withId(R.id.bigDollarSign))
				.check(matches(isDisplayed()));
	}
}

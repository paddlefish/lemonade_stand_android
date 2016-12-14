package net.paddlefish.lemonadestand;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * So we can run LeakCanary
 *
 * Created by arahn on 12/14/16.
 */

public class LemonadeStandApp extends Application {
	@Override public void onCreate() {
		super.onCreate();
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
		// Normal app init code...
	}
}

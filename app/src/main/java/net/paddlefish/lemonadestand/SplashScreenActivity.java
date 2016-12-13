package net.paddlefish.lemonadestand;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;

public class SplashScreenActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);

		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				final View lemonImageView = findViewById(R.id.imageView);
				final View appNameView = findViewById(R.id.appNameTextView);

				Intent settingsIntent = new Intent(SplashScreenActivity.this, MainActivity.class);

				ActivityOptions transitionAnimation = ActivityOptions.makeSceneTransitionAnimation(
						SplashScreenActivity.this,
						Pair.create(lemonImageView, "lemon"),
						Pair.create(appNameView, "appname"));
				Bundle bundle = transitionAnimation.toBundle();
				startActivity(settingsIntent, bundle);
			}
		}, 1000);

	}

}

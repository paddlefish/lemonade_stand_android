package net.paddlefish.lemonadestand;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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

				Intent settingsIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
				ActivityOptions transitionAnimation = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, lemonImageView, "lemon");
				Bundle bundle = transitionAnimation.toBundle();
				startActivity(settingsIntent, bundle);
			}
		}, 1000);

	}

}

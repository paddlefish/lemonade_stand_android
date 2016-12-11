package net.paddlefish.lemonadestand.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utility for saving current score to service.
 *
 * Created by arahn on 12/11/16.
 */

public class HighScoreService {
	private static HighScoreService sharedInstance = new HighScoreService();
	private ThreadPoolExecutor mExecutor = null;

	private HighScoreService() {}

	private ThreadPoolExecutor getExecutor() {
		if (mExecutor == null) {
			mExecutor = new ThreadPoolExecutor(1, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		}
		return mExecutor;
	}

	public static void saveHighScore(final int score) {
		// FIXME: Implement this method.  It should use a background thread to call saveHighScoreSync
		// and pass in the score.
	}

	@SuppressLint("DefaultLocale")
	private static void saveHighScoreSync(int score) {
		String myurl = "http://paddlefish.net/evine_training/high_score.php";

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(false);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(String.format("high_score=%d", score));
			writer.flush();
			writer.close();
			os.close();


			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("GameSetupService", "The response is: " + response);

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch(Throwable t) {
			Log.e("HighScoreService", "Could not save high score");
		}

	}
}

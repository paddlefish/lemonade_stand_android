package net.paddlefish.lemonadestand.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameSetupService implements Handler.Callback {
	final private Handler mHandler;
	final private int MSG_FETCH_HIGH_SCORES = 1;
	private int nextRequestId = 0;
	private SparseArray<HighScoreCallback> mRequestMap = new SparseArray<>();

	public GameSetupService() {
		HandlerThread handlerThread = new HandlerThread("Game Setup Service");
		handlerThread.start();
		mHandler = new Handler(handlerThread.getLooper(), this);
	}

	public interface HighScoreCallback {
		void onHighScore(int highScore);
	}

	/**
	 * Fetch the latest highscore from the highscore server.
	 * In the event of a network failure a default high score is sent instead.
	 *
	 * @param callback the function to call with the result.
	 */
	public void fetchHighScore(HighScoreCallback callback) {
		// TODO: Route this message to our dedicated thread,
		// call downloadHighScore to get the high score
		// and send result back to the Main thread via dispatchHighScoreResult

		// For now just dispatch a fake result right away.
		// FIXME: Delete this line after implementing the correct solution.
		callback.onHighScore(10000);
	}

	/**
	 * Send the high score back from the background thread to the main thread.
	 * @param requestId the request id this result is for
	 * @param highScore the result (the highscore from the highscore server)
	 */
	private void dispatchHighScoreResult(final int requestId, final int highScore) {
		Handler uiHandler = new Handler(Looper.getMainLooper());
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				HighScoreCallback callback = mRequestMap.get(requestId);
				if (callback != null) {
					callback.onHighScore(highScore);
				}
				mRequestMap.remove(requestId);
			}
		});
	}

	/**
	 * Synchronously connects to the internet and uses HTTP GET to download a
	 * JSON file, parse it and extracts the high score string.
	 *
	 * @return int the current worldwide high score
	 */
	private int downloadHighScore() {
		String myurl = "http://paddlefish.net/evine_training/high_scores.json";
		InputStream is = null;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("GameSetupService", "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			return readIt(is);

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch(Throwable t) {
			return 100;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable t) {
					Log.e("GameSetupService", "Error cleaning up in finally");
				}
			}
		}

	}

	private int readIt(InputStream stream) throws IOException {
		Reader reader = new InputStreamReader(stream, "UTF-8");
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.beginObject();
		int result = 100;
		while (jsonReader.hasNext()) {
			String name = jsonReader.nextName();
			if (name.equals("high_score")) {
				result = jsonReader.nextInt();
			}
		}
		jsonReader.close();
		return result;

	}

	/**
	 * Override method from Handler.Callback
	 * @param message The Message that says what to do.
	 * @return true if the message was handled
	 */
	@Override
	public boolean handleMessage(Message message) {
		switch (message.what) {
			case MSG_FETCH_HIGH_SCORES:
				final int highScore = downloadHighScore();
				dispatchHighScoreResult(message.arg1, highScore);
				return true;
		}
		return false;
	}
}
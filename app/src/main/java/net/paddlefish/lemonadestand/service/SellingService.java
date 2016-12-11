package net.paddlefish.lemonadestand.service;

import android.os.AsyncTask;
import android.util.Log;

import net.paddlefish.lemonadestand.utils.Cancellable;
import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.utils.AsyncTaskCancellable;

import static java.lang.Math.ceil;

public class SellingService {

	public interface SellingCallback {
		void progress(int hourOfDay, int numGlassesSold);

		void done(int totalNumSold);
	}

	private static class Progress {
		final int hourOfDay;
		final int numGlassesSold;

		Progress(int hourOfDay, int numGlassesSold) {
			this.hourOfDay = hourOfDay;
			this.numGlassesSold = numGlassesSold;
		}
	}

	private static class Params {
		final GameState gameState;
		final int price;

		Params(GameState gameState, int price) {
			this.gameState = gameState;
			this.price = price;
		}
	}

	private static class Result {
		final int numSold;

		Result(int numSold) {
			this.numSold = numSold;
		}
	}

	private static class MyTask extends AsyncTask<Params, Progress, Result> {
		final SellingCallback callback;

		MyTask(Params params, SellingCallback callback) {
			super();
			this.callback = callback;
			execute(params);
		}
		@Override
		protected Result doInBackground(Params... paramses) {
			int numSold[] = new int[paramses.length];
			int totalNumSold = 0;
			for(int hour = 0; hour < 8; hour++) {
				// Lemonade stand is open for 8 hours
				for (int i = 0; i < paramses.length; i++) {
					Params params = paramses[i];
					if (isCancelled()) {
						Log.e("ABR", "doInBackground but isCancelled()!");
						break;
					}
					double saleProbability = computeSaleProbability(params.gameState.lemonadeBaseCostPerGlass(), params.price);
					int numGlassesLeft = params.gameState.getLemonade() - numSold[i];
					int numSoldThisHour = (int) ceil(numGlassesLeft * saleProbability);
					numSold[i] += numSoldThisHour;
					totalNumSold += numSoldThisHour;
				}
				publishProgress(new Progress(hour, totalNumSold));
				try {
					Thread.sleep(400);
				}
				catch (InterruptedException ie) {
					Log.e("ABR", "Selling task... InterruptedException!");
					break;
				}
			}
			try {
				Thread.sleep(400);
			}
			catch (InterruptedException ie) {
				Log.e("ABR", "Selling task... InterruptedException AGAIN??!");
			}
			return new Result(totalNumSold);
		}

		private double computeSaleProbability(int cost, int price) {
			double sellRate = 1.0;
			if (price == 99999) {
				sellRate = 1.0;
			}
			else if (price > 4 * cost) {
				sellRate = 0.0;
			}
			else if (price > 3 * cost) {
				sellRate = 0.1;
			}
			else if (price > 2 * cost) {
				sellRate = 0.4;
			}
			else {
				sellRate = 0.5;
			}
			sellRate = sellRate * (0.8 + Math.random() * 0.4);
			sellRate = Math.min(1.0, sellRate);
			return sellRate;
		}

		@Override
		protected void onPostExecute(Result result) {
			super.onPostExecute(result);
			callback.done(result.numSold);
		}

		@Override
		protected void onProgressUpdate(Progress... values) {
			super.onProgressUpdate(values);
			for (Progress progress:values) {
				callback.progress(progress.hourOfDay, progress.numGlassesSold);
			}
		}
	}

	public Cancellable sellLemonade(GameState gameState, int price, final SellingCallback callback) {
		final MyTask task = new MyTask(new Params(gameState, price), callback);
		return new AsyncTaskCancellable(task);
	}
}

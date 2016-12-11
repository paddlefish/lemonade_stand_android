package net.paddlefish.lemonadestand.utils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Cancellable for AsyncTask
 *
 * Created by arahn on 12/10/16.
 */

public class AsyncTaskCancellable implements Cancellable {
	final private Object mMutex;
	final private AsyncTask mTask;
	private boolean mCancelled;

	public AsyncTaskCancellable(AsyncTask task) {
		mTask = task;
		mCancelled = false;
		mMutex = new Object();
	}

	@Override
	public boolean cancel() {
		Log.e("ABR", "Cancelling task");
		synchronized (mMutex) {
			if (!mCancelled) {
				mCancelled = true;
				mTask.cancel(true);
				return true;
			}
		}
		return false;
	}

}

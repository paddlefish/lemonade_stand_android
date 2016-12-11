package net.paddlefish.lemonadestand.utils;

import android.app.ProgressDialog;

/**
 * Cancellable for ProgressDialog
 *
 * Created by arahn on 12/10/16.
 */

public class ProgressDialogCancellable implements Cancellable {
		final private Object mMutex;
		private ProgressDialog mTask;

		public ProgressDialogCancellable(ProgressDialog task) {
			mTask = task;
			mMutex = new Object();
		}

		@Override
		public boolean cancel() {
			synchronized (mMutex) {
				if (mTask != null) {
					mTask.dismiss();
					mTask = null;
					return true;
				}
			}
			return false;
		}

}

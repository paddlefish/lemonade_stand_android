package net.paddlefish.lemonadestand.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.SparseArray;

import net.paddlefish.lemonadestand.R;
import net.paddlefish.lemonadestand.SurveyActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AnnoyingPromptService extends IntentService {
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_ANNOY_USER_LATER = "net.paddlefish.lemonadestand.service.action.ANNOY_USER_LATER";

	private static final String PARAM_ANNOYING_MESSAGE = "net.paddlefish.lemonadestand.service.extra.ANNOYING_MESSAGE";
	private static final String PARAM_RESPONSE_ID = "net.paddlefish.lemonadestand.service.extra.RESPONSE_ID";

	public AnnoyingPromptService() {
		super("AnnoyingPromptService");
	}

	/**
	 * Starts this service to perform action Foo with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 *
	 * @see IntentService
	 */
	public static void startActionSendAnnoyingMessageLater(Context context, String message, int responseId) {
		Intent intent = new Intent(context, AnnoyingPromptService.class);
		intent.setAction(ACTION_ANNOY_USER_LATER);
		intent.putExtra(PARAM_ANNOYING_MESSAGE, message);
		intent.putExtra(PARAM_RESPONSE_ID, responseId);
		context.startService(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			switch (action) {
				default:
					break;
			}
		}
	}

	/**
	 * Handle action Foo in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionAnnoyUserLater(final String message, final int responseId) {
		Handler uiHandler = new Handler(Looper.getMainLooper());
		uiHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Context ctx = AnnoyingPromptService.this;
				Intent intent = new Intent(ctx, SurveyActivity.class);
				intent.putExtra("responseId", responseId);
				PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

				NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

				b.setAutoCancel(true)
						.setDefaults(Notification.DEFAULT_ALL)
						.setWhen(System.currentTimeMillis())
						.setSmallIcon(R.mipmap.ic_launcher)
						.setTicker("Lemonade Stand")
						.setContentTitle("Lemonade Stand needs your feedback.")
						.setContentText(message)
						.setDefaults(Notification.DEFAULT_LIGHTS)
						.setContentIntent(contentIntent)
						.setContentInfo("Info");

				NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(1, b.build());

			}
		}, 10000);
	}
}

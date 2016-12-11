package net.paddlefish.lemonadestand;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.paddlefish.lemonadestand.utils.Cancellable;
import net.paddlefish.lemonadestand.utils.CancellationPool;
import net.paddlefish.lemonadestand.model.GameGroceries;
import net.paddlefish.lemonadestand.model.GameModel;
import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;
import net.paddlefish.lemonadestand.service.PurchasingService;
import net.paddlefish.lemonadestand.utils.ProgressDialogCancellable;

public class MainActivity extends AppCompatActivity implements
		HelloFragment.OnFragmentInteractionListener,
		MoneyFragment.OnFragmentInteractionListener,
		ShoppingFragment.OnFragmentInteractionListener,
		MakeLemonadeFragment.OnFragmentInteractionListener,
		OutOfMoneyFragment.OnFragmentInteractionListener,
		DoneSellingFragment.OnFragmentInteractionListener {

	private boolean isActive;
	private CancellationPool mCancellationPool = new CancellationPool();
	private final static String PARAM_SAVED_GAME_SCREEN_CODE = "PARAM_SAVED_GAME_SCREEN_CODE";
	private final static String PARAM_SAVED_GAME_STATE = "PARAM_SAVED_GAME_STATE";

	/*
		You must implement the onCreate() method to perform basic application startup logic
		that should happen only once for the entire life of the activity. For example, your
		implementation of onCreate() should define the user interface and possibly instantiate
		some class-scope variables.

		The system calls this method -- you should never call it EXCEPT you must call super's as shown here.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar); // Attaching the layout to the toolbar object
		setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActive = true;
		ViewGroup frame = (ViewGroup) findViewById(R.id.placeholder_view);
		if (frame.getChildCount() == 0) {
			switchToHelloScreen();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isActive = false;
		mCancellationPool.cancel();
	}

	private void switchToScreen(GameScreen screen, GameModel gameModel) {
		switchToScreen(screen, gameModel, 0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, RulesActivity.class);
			startActivity(settingsIntent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean switchToScreen(GameScreen screen, GameModel gameModel, int moneyEarned, int glassesWasted) {

		switch (screen) {
			case HELLO:
				return switchToHelloScreen();
			case MONEY:
				return switchToMoneyScreen(gameModel);
			case SHOPPING:
				return switchToShoppingScreen(gameModel);
			case MAKING:
				return switchToMakeLemonadeScreen(gameModel);
			case DONE_SELLING:
				return switchToDoneSellingScreen(gameModel, moneyEarned, glassesWasted);
			case OUT_OF_MONEY:
				return switchToOutOfMoneyScreen();
		}
		return false;
	}

	private boolean switchToFragment(Fragment nextFragment) {
		return switchToFragment(nextFragment, true);
	}

	private boolean switchToFragment(Fragment nextFragment, boolean addToBackStack) {
		if (!isActive) {
			// The activity is not active; we cannot switch to a new fragment.
			return false;
		}

		FragmentManager manager = getSupportFragmentManager();
		if (!addToBackStack) {
			for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
				manager.popBackStack();
			}
		}
		FragmentTransaction transaction = manager
				.beginTransaction()
				.replace(R.id.placeholder_view, nextFragment);

		if (addToBackStack) {
			transaction.addToBackStack(null);

		}
		transaction.commit();

		return true;
	}

	boolean switchToHelloScreen() {
		return switchToFragment(HelloFragment.newInstance(10000), false);
	}
	boolean switchToOutOfMoneyScreen() {
		return switchToFragment(OutOfMoneyFragment.newInstance());
	}

	boolean switchToMoneyScreen(GameModel gameModel) {
		return switchToFragment(MoneyFragment.newInstance(gameModel.getGameState()));
	}

	boolean switchToShoppingScreen(GameModel gameModel) {
		return switchToFragment(ShoppingFragment.newInstance(gameModel.getGameState()));
	}

	boolean switchToMakeLemonadeScreen(GameModel gameModel) {
		return switchToFragment(MakeLemonadeFragment.newInstance(gameModel.getGameState()));
	}

	boolean switchToDoneSellingScreen(GameModel gameModel, int moneyEarned, int glassesWasted) {
		return switchToFragment(DoneSellingFragment.newInstance(gameModel.getGameState(), moneyEarned, glassesWasted));
	}

	@Override
	public void startGame() {
		switchToScreen(GameScreen.MONEY, new GameModel(1000));
	}

	@Override
	public void goShoppingButtonPressed(IGameState gameState) {
		switchToScreen(GameScreen.SHOPPING, new GameModel(gameState));
	}

	@Override
	public void buyGroceriesButtonPressed(IGameState previousState, GameGroceries qtyOrdered) {
		final GameModel gameModel = new GameModel(previousState);

		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		dialog.show();
		final Cancellable progressCancellable = new ProgressDialogCancellable(dialog);

		gameModel.buySome(qtyOrdered, new PurchasingService.PurchaseCompletion() {
			@Override
			public void result(boolean success) {
				progressCancellable.cancel();
				if (success) {
					gameModel.makeLemonade();
					switchToScreen(GameScreen.MAKING, gameModel);
				}
				else {
					switchToScreen(GameScreen.OUT_OF_MONEY, gameModel);
				}
			}
		});
		mCancellationPool.addCancellable(progressCancellable);
	}

	@Override
	public void sellLemonade(IGameState previousState, int price) {
		final GameModel  gameModel = new GameModel(previousState);

		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setIndeterminate(false);
		dialog.setProgress(0);
		dialog.setMessage("Opening shop...");
		dialog.show();
		final Cancellable progressCancellable = new ProgressDialogCancellable(dialog);

		final Cancellable task = gameModel.sellLemonade(price, new GameModel.SalesResults.Callback() {
			@Override
			public void saleCompleted(GameModel.SalesResults results) {
				progressCancellable.cancel();
				switchToScreen(GameScreen.DONE_SELLING, gameModel, results.moneyEarned, results.glassesWasted);
			}

			@Override
			public void saleInProgress(int hour, int numSold) {
				dialog.setProgress(100 * hour / 7);
				dialog.setMessage(String.format("%d:00.  %d glasses sold", (hour + 8) % 12 + 1, numSold));
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				task.cancel();
			}
		});
		mCancellationPool.addCancellable(task);
		mCancellationPool.addCancellable(progressCancellable);
	}

	@Override
	public void playAgain() {
		switchToScreen(GameScreen.HELLO, null);
	}

	@Override
	public void restUp(IGameState previousState) {
		GameModel  gameModel = new GameModel(previousState);
		switchToScreen(GameScreen.MONEY, gameModel);
	}

	@Override
	public void restartGame() {
		switchToScreen(GameScreen.HELLO, null);
	}
}

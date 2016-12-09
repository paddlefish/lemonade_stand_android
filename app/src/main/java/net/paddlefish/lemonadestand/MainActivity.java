package net.paddlefish.lemonadestand;

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

import net.paddlefish.lemonadestand.model.GameGroceries;
import net.paddlefish.lemonadestand.model.GameModel;
import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
		HelloFragment.OnFragmentInteractionListener,
		MoneyFragment.OnFragmentInteractionListener,
		ShoppingFragment.OnFragmentInteractionListener,
		MakeLemonadeFragment.OnFragmentInteractionListener,
		OutOfMoneyFragment.OnFragmentInteractionListener,
		DoneSellingFragment.OnFragmentInteractionListener {

	private GameState mGameState;
	private GameScreen mCurrentScreen;
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
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onCreate was called");
		Log.e("JG", "onCreate -- savedInstanceState null? - " + (savedInstanceState == null));
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar); // Attaching the layout to the toolbar object
		setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

		if (savedInstanceState == null) {
			switchToScreen(GameScreen.HELLO, new GameModel(null));
		} else {
			String code = savedInstanceState.getString(PARAM_SAVED_GAME_SCREEN_CODE);
			if (code != null) {
				mCurrentScreen = GameScreen.screenForCode(code);
			}
			GameState savedState = savedInstanceState.getParcelable(PARAM_SAVED_GAME_STATE);
			if (savedState != null) {
				mGameState = savedState;
			}
		}
	}

	/*
		The very last callback is onDestroy(). The system calls this method on your activity
		as the final signal that your activity instance is being completely removed from
		the system memory.

		Most apps don't need to implement this method -- clean up should be handled in
		onPause or onStop

		The system calls this method -- you should never call it EXCEPT you must call super's as shown here.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onDestroy was called");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// I put this here in case you want to add an informative Log statement
		super.onRestoreInstanceState(savedInstanceState);
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onRestoreInstanceState was called");
		Log.e("JG", "onRestoreInstanceState -- savedInstanceState null? - " + (savedInstanceState == null));
	}

	/*
			The system calls this method when the user is leaving your activity and passes it the
			Bundle object that will be saved in the event that your activity is destroyed. If the
			system must recreate the activity instance later, it passes the same Bundle object to
			both the onRestoreInstanceState() and onCreate() methods
		*/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(PARAM_SAVED_GAME_STATE, mGameState);
		outState.putString(PARAM_SAVED_GAME_SCREEN_CODE, mCurrentScreen.screenCode);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(outState);
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onSaveInstanceState was called");
	}

	@Override
	protected void onStart() {

		super.onStart();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onStart was called");
	}

	@Override
	protected void onStop() {

		super.onStop();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onStop was called");
	}

	@Override
	protected void onResume() {

		super.onResume();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onResume was called");
	}

	@Override
	protected void onPause() {

		super.onPause();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onPause was called");
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		Log.e("JG", MainActivity.class.getCanonicalName() + ".onRestart was called");
	}

	private void switchToScreen(GameScreen screen, GameModel gameModel) {
		mCurrentScreen = screen;
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

	private void switchToScreen(GameScreen screen, GameModel gameModel, int moneyEarned, int glassesWasted) {

		mGameState = gameModel.getGameState();
		switch (screen) {
			case HELLO:
				switchToHelloScreen();
				break;
			case MONEY:
				switchToMoneyScreen(gameModel);
				break;
			case SHOPPING:
				switchToShoppingScreen(gameModel);
				break;
			case MAKING:
				switchToMakeLemonadeScreen(gameModel);
				break;
			case DONE_SELLING:
				switchToDoneSellingScreen(gameModel, moneyEarned, glassesWasted);
				break;
			case OUT_OF_MONEY:
				switchToOutOfMoneyScreen();
				break;
		}
	}

	private void switchToFragment(Fragment nextFragment) {
		switchToFragment(nextFragment, true);
	}

	private void switchToFragment(Fragment nextFragment, boolean addToBackStack) {
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
	}

	void switchToHelloScreen() {
		mGameState = null;
		switchToFragment(HelloFragment.newInstance(10000), false);
	}
	void switchToOutOfMoneyScreen() {
		mGameState = null;
		switchToFragment(OutOfMoneyFragment.newInstance());
	}

	void switchToMoneyScreen(GameModel gameModel) {
		switchToFragment(MoneyFragment.newInstance(gameModel.getGameState()));
	}

	void switchToShoppingScreen(GameModel gameModel) {
		switchToFragment(ShoppingFragment.newInstance(gameModel.getGameState()));
	}

	void switchToMakeLemonadeScreen(GameModel gameModel) {
		switchToFragment(MakeLemonadeFragment.newInstance(gameModel.getGameState()));
	}

	void switchToDoneSellingScreen(GameModel gameModel, int moneyEarned, int glassesWasted) {
		switchToFragment(DoneSellingFragment.newInstance(gameModel.getGameState(), moneyEarned, glassesWasted));
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
		GameModel  gameModel = new GameModel(previousState);
		if (gameModel.buySome(qtyOrdered)) {
			gameModel.makeLemonade();
			switchToScreen(GameScreen.MAKING, gameModel);
		}
		else {
			switchToScreen(GameScreen.OUT_OF_MONEY, gameModel);
		}
	}

	@Override
	public void sellLemonade(IGameState previousState, int price) {
		GameModel  gameModel = new GameModel(previousState);
		int cost = gameModel.lemonadeBaseCostPerGlass();
		double sellRate = 1.0;
		if (price == 99999) {
			sellRate = 1.0;
		}
		else if (price > 4 * cost) {
			sellRate = 0.0;
		}
		else if (price > 3 * cost) {
			sellRate = 0.2;
		}
		else if (price > 2 * cost) {
			sellRate = 0.7;
		}
		sellRate = sellRate * (0.8 + Math.random() * 0.4);
		sellRate = Math.min(1.0, sellRate);
		GameModel.SalesResults sale = gameModel.sellLemonade(sellRate, price);
		switchToScreen(GameScreen.DONE_SELLING, gameModel, sale.moneyEarned, sale.glassesWasted);
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

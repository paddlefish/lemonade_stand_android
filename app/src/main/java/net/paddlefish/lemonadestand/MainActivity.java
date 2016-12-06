package net.paddlefish.lemonadestand;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.paddlefish.lemonadestand.model.GameGroceries;
import net.paddlefish.lemonadestand.model.GameModel;
import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

enum GameScreen {
	HELLO ("hello"),
	MONEY ("money"),
	SHOPPING ("shopping"),
	MAKING ("making"),
	DONE_SELLING ("done_selling"),
	OUT_OF_MONEY ("out_of_money");

	final String screenCode;
	private static final Map<String, GameScreen> ELEMENTS;
	GameScreen(String screenCode) {
		this.screenCode = screenCode;
	}

	static {
		Map<String, GameScreen> elements = new HashMap<>();
		for (GameScreen value : values()) {
			elements.put(value.screenCode, value);
		}
		ELEMENTS = Collections.unmodifiableMap(elements);
	}

	public static GameScreen screenForCode(String screenCode) {
		GameScreen result = ELEMENTS.get(screenCode);
		if (result != null) {
			return result;
		}
		return HELLO;
	}
}

public class MainActivity extends AppCompatActivity implements
		HelloFragment.OnFragmentInteractionListener,
		MoneyFragment.OnFragmentInteractionListener,
		ShoppingFragment.OnFragmentInteractionListener,
		MakeLemonadeFragment.OnFragmentInteractionListener,
		OutOfMoneyFragment.OnFragmentInteractionListener,
		DoneSellingFragment.OnFragmentInteractionListener {

	private GameState mGameState;
	private String currentScreen;
	private final static String PARAM_SAVED_GAME_SCREEN_CODE = "PARAM_SAVED_GAME_SCREEN_CODE";
	private final static String PARAM_SAVED_GAME_STATE = "PARAM_SAVED_GAME_STATE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			switchToScreen(GameScreen.HELLO, new GameModel(null));
		} else {
			GameScreen currentScreen = null;
			GameState savedState = null;
			String code = savedInstanceState.getString(PARAM_SAVED_GAME_SCREEN_CODE);
			if (code != null) {
				currentScreen = GameScreen.screenForCode(code);
			}
			savedState = savedInstanceState.getParcelable(PARAM_SAVED_GAME_STATE);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(PARAM_SAVED_GAME_STATE, mGameState);
		outState.putString(PARAM_SAVED_GAME_SCREEN_CODE, currentScreen);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		outState.putParcelable(PARAM_SAVED_GAME_STATE, mGameState);
		outState.putString(PARAM_SAVED_GAME_SCREEN_CODE, currentScreen);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void switchToScreen(GameScreen screen, GameModel gameModel) {
		currentScreen = screen.screenCode;
		switchToScreen(screen, gameModel, 0, 0);
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
				.replace(R.id.activity_main, nextFragment);

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

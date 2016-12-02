package net.paddlefish.lemonadestand;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.paddlefish.lemonadestand.model.GameGroceries;
import net.paddlefish.lemonadestand.model.GameModel;
import net.paddlefish.lemonadestand.model.IGameState;

public class MainActivity extends AppCompatActivity implements
		HelloFragment.OnFragmentInteractionListener,
		MoneyFragment.OnFragmentInteractionListener,
		ShoppingFragment.OnFragmentInteractionListener,
		MakeLemonadeFragment.OnFragmentInteractionListener,
		OutOfMoneyFragment.OnFragmentInteractionListener,
		DoneSellingFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();

		switchToHelloScreen();
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
		switchToFragment(HelloFragment.newInstance(10000), false);
	}
	void switchToOutOfMoneyScreen() {
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
		switchToMoneyScreen(new GameModel(1000));
	}

	@Override
	public void goShoppingButtonPressed(IGameState gameState) {
		switchToShoppingScreen(new GameModel(gameState));
	}

	@Override
	public void buyGroceriesButtonPressed(IGameState previousState, GameGroceries qtyOrdered) {
		GameModel  gameModel = new GameModel(previousState);
		if (gameModel.buySome(qtyOrdered)) {
			gameModel.makeLemonade();
			switchToMakeLemonadeScreen(gameModel);
		}
		else {
			switchToOutOfMoneyScreen();
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
		switchToDoneSellingScreen(gameModel, sale.moneyEarned, sale.glassesWasted);
	}

	@Override
	public void playAgain() {
		switchToHelloScreen();
	}

	@Override
	public void restUp(IGameState previousState) {
		GameModel  gameModel = new GameModel(previousState);
		switchToMoneyScreen(gameModel);
	}

	@Override
	public void restartGame() {
		switchToHelloScreen();
	}
}

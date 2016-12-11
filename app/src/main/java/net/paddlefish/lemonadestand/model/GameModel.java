package net.paddlefish.lemonadestand.model;


import net.paddlefish.lemonadestand.service.PurchasingService;
import net.paddlefish.lemonadestand.service.SellingService;
import net.paddlefish.lemonadestand.utils.Cancellable;

/**
 * Game logic for Lemonade Stand. Tracks how much money, lemons, sugar and ice is on hand.
 * Has methods for making and selling lemonade.
 * Has properties for determining game win.
 * Created by arahn on 12/1/16.
 */

public class GameModel extends GameModelBase implements IGameState {

	/**
	 * Create a new game model, with a starting amount of money.
	 *
	 * @param initialMoney How much money to start the game with
	 */
	public GameModel(int initialMoney) {
		int DEFAULT_LEMON_PRICE = 50;
		int DEFAULT_SUGAR_PRICE = 100;
		int DEFAULT_ICE_PRICE = 30;
		this.money = initialMoney;
		this.prices = new GameGroceries(DEFAULT_LEMON_PRICE, DEFAULT_SUGAR_PRICE, DEFAULT_ICE_PRICE);
		this.inventory = new GameGroceries(0, 0, 0);
	}

	public GameModel(IGameState gameState) {
		this(0);
		if (gameState == null) {
			return;
		}
		this.money = gameState.getMoney();
		this.prices = gameState.getPrices();
		this.inventory = gameState.getInventory();
		this.lemonade = gameState.getLemonade();
	}

	/**
	 * How much money the user has, in cents
	 */
	int money;

	/**
	 * How many glasses of lemonade
	 */
	int lemonade;

	GameGroceries inventory;

	GameGroceries prices;

	/**
	 * Get the amount of money the user has
	 * @return money in cents
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Get the number of glasses
	 * @return glasses of lemonade
	 */
	@Override
	public int getLemonade() {
		return lemonade;
	}

	/**
	 * Set the prices
	 */
	void setPrices(GameGroceries newPrices) {
		this.prices = newPrices;
	}

	/**
	 * Get the qty on hand for sugar, ice, and lemons
	 * @return the qty available for everything
	 */
	public GameGroceries getInventory() {
		return inventory;
	}

	/**
	 * Get the prices for sugar, ice, and lemons
	 * @return the prices for everything
	 */
	public GameGroceries getPrices() {
		return prices;
	}

	/**
	 * Make lemonade, converting as much of the inventory
	 * to lemonade as possible.
	 */
	public void makeLemonade() {
		// The recipe is four lemons, 2 cups of sugar and 1 pound of ice to make 1 gallon
		double howMuchLemonsLemonade = inventory.getLemons() / LEMON_RECIPE;
		double howMuchSugarLemonade = inventory.getSugar() / SUGAR_RECIPE;
		double howMuchIceLemonade = inventory.getIce() / ICE_RECIPE;

		double howMuchLemonade = Math.min(Math.min(howMuchIceLemonade, howMuchLemonsLemonade), howMuchSugarLemonade);

		int deltaLemons = (int) (howMuchLemonade * LEMON_RECIPE);
		int deltaSugar = (int) (howMuchLemonade * SUGAR_RECIPE);
		int deltaIce = (int) (howMuchLemonade * ICE_RECIPE);

		int lemons = inventory.getLemons() - deltaLemons;
		int sugar = inventory.getSugar() - deltaSugar;
		int ice = inventory.getIce() - deltaIce;

		inventory = new GameGroceries(lemons, sugar, ice);

		lemonade += (int) Math.floor(howMuchLemonade * NUM_CUPS_IN_GALLON);
	}

	public static class SalesResults {
		public final int moneyEarned;
		public final int glassesWasted;

		SalesResults(int moneyEarned, int glassesWasted) {
			this.moneyEarned = moneyEarned;
			this.glassesWasted = glassesWasted;
		}

		public interface Callback {
			void saleCompleted(SalesResults results);
			void saleInProgress(int hour, int numSold);
		}
	}

	/**
	 * Sell lemonade, selling to a certain percentage of
	 * possible sales. 100% means you sell out.
	 * @param price price per glass of lemonade in cents
	 * @param callback callback object for collecting results
	 */
	public Cancellable sellLemonade(final int price, final SalesResults.Callback callback) {
		SellingService service = new SellingService();
		return service.sellLemonade(getGameState(), price, new SellingService.SellingCallback() {
			@Override
			public void progress(int hourOfDay, int numGlassesSold) {
				callback.saleInProgress(hourOfDay, numGlassesSold);
			}

			@Override
			public void done(int totalNumSold) {
				int originalNumLemonade = lemonade;
				int wastedLemonade = originalNumLemonade - totalNumSold;
				lemonade = 0;
				money += totalNumSold * price;
				callback.saleCompleted(new SalesResults(price * totalNumSold, wastedLemonade));
			}
		});
	}

	/**
	 * Buy groceries.
	 * @param quantities How much of each grocery item to buy
	 * @param completion callback to send results about whether there was enough money to buy them all.
	 *                   If there is not enough money then no groceries are purchased.
	 */
	public void buySome(final GameGroceries quantities, final PurchasingService.PurchaseCompletion completion) {
		final Accessor[] accessors = new Accessor[] { new Accessor.Lemon(), new Accessor.Sugar(), new Accessor.Ice() };

		final PurchasingService purchasingService = new PurchasingService();

		final int totalPrice = purchasingService.calculatePriceForGroceries(quantities, prices);

		if (money < totalPrice) {
			completion.result(false);
			return;
		}

		new PurchasingService().purchaseGroceries(quantities, totalPrice, prices, new PurchasingService.PurchaseCompletion() {
			@Override
			public void result(boolean success) {
				if (success) {
					GameGroceries newInventory = inventory;
					for (Accessor accessor: accessors) {
						int curQty = accessor.get(newInventory);
						newInventory = accessor.set(newInventory, curQty + accessor.get(quantities));
					}
					inventory = newInventory;
					money -= totalPrice;
				}
				completion.result(success);
			}
		});
	}

	/**
	 * Get the current game state.
	 */
	public GameState getGameState() {
		return new GameState(this);
	}
}

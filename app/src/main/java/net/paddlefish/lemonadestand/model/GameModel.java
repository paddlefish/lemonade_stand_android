package net.paddlefish.lemonadestand.model;


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

	}
	/**
	 * Sell lemonade, selling to a certain percentage of
	 * possible sales. 100% means you sell out.
	 * @param saleRate a value between 0.0 and 1.0
	 * @param price price per glass of lemonade in cents
	 */
	public SalesResults sellLemonade(double saleRate, int price) {

		double howMuchLemonade = lemonade;
		int numGlassesSold = (int) (howMuchLemonade * saleRate);
		int numWasted = lemonade - numGlassesSold;

		int deltaMoney = numGlassesSold * price;

		lemonade = 0;
		money += deltaMoney;

		return new SalesResults(deltaMoney, numWasted);
	}

	/**
	 * Buy groceries.
	 * @param quantities How much of each grocery item to buy
	 * @return whether there was enough money to buy them all. If there is not enough money
	 * then no groceries are purchased.
	 */
	public boolean buySome(GameGroceries quantities) {
		Accessor[] accessors = new Accessor[] { new Accessor.Lemon(), new Accessor.Sugar(), new Accessor.Ice() };

		int totalPrice = 0;
		for (Accessor accessor: accessors) {
			totalPrice += accessor.getPrice(this) * accessor.get(quantities);
		}
		if (money < totalPrice) {
			return false;
		}
		GameGroceries inventory = this.inventory;
		for (Accessor accessor: accessors) {
			int curQty = accessor.get(inventory);
			inventory = accessor.set(inventory, curQty + accessor.get(quantities));
		}
		this.inventory = inventory;
		money -= totalPrice;
		return true;
	}

	/**
	 * Get the current game state.
	 */
	public GameState getGameState() {
		return new GameState(this);
	}
}

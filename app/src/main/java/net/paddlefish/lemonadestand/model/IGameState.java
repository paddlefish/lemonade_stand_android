package net.paddlefish.lemonadestand.model;

public interface IGameState {
	int getMoney();

	/**
	 * The number of glasses of lemonade that was made for today.
	 * @return how many glasses of lemonade are on hand.
	 */
	int getLemonade();

	GameGroceries getPrices();

	GameGroceries getInventory();
}

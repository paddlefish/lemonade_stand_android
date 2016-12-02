package net.paddlefish.lemonadestand.model;

abstract class GameModelBase implements IGameState {
	final static double LEMON_RECIPE = 4.0;
	final static double SUGAR_RECIPE = 2.0;
	final static double ICE_RECIPE = 1.0;
	final static int NUM_CUPS_IN_GALLON = 16;

	public int lemonadeBaseCostPerGlass() {
		GameGroceries prices = getPrices();
		double costPerGallon = LEMON_RECIPE * prices.getLemons()
				+ SUGAR_RECIPE * prices.getSugar()
				+ ICE_RECIPE * prices.getIce();
		return (int) Math.floor(costPerGallon / NUM_CUPS_IN_GALLON);
	}
}

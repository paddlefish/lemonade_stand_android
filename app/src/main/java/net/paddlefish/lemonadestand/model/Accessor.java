package net.paddlefish.lemonadestand.model;

import net.paddlefish.lemonadestand.R;

public abstract class Accessor {
	public static Accessor accessorForRow(int i) {
		switch (i) {
			case 1: return new Lemon();
			case 2: return new Sugar();
			default: return new Ice();
		}
	}

	public abstract int get(GameGroceries groceries);

	public abstract GameGroceries set(GameGroceries g, int newValue);

	public abstract int productName();

	public abstract int details();

	public int getInventory(IGameState state) {
		return get(state.getInventory());
	}

	public int getPrice(IGameState state) {
		return get(state.getPrices());
	}

	static class Lemon extends Accessor {
		public int productName() { return R.string.product_name_lemons; }
		public int details() { return R.string.product_details_lemons; }
		public int get(GameGroceries groceries) { return groceries.getLemons(); }
		public GameGroceries set(GameGroceries g, int newValue) {
			return new GameGroceries(newValue, g.getSugar(), g.getIce());
		}
	}

	static class Sugar extends Accessor {
		public int productName() { return R.string.product_name_sugar; }
		public int details() { return R.string.product_details_sugar; }
		public int get(GameGroceries groceries) { return groceries.getSugar(); }
		public GameGroceries set(GameGroceries g, int newValue) {
			return new GameGroceries(g.getLemons(), newValue, g.getIce());
		}
	}

	static class Ice extends Accessor {
		public int productName() { return R.string.product_name_ice; }
		public int details() { return R.string.product_details_ice; }
		public int get(GameGroceries groceries) { return groceries.getIce(); }
		public GameGroceries set(GameGroceries g, int newValue) {
			return new GameGroceries(g.getLemons(), g.getSugar(), newValue);
		}
	}
}

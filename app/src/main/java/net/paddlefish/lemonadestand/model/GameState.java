package net.paddlefish.lemonadestand.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.parceler.ParcelConstructor;

@org.parceler.Parcel
public final class GameState extends GameModelBase implements IGameState {
	final GameGroceries inventory;
	final GameGroceries prices;
	final int money;
	final int lemonade;

	@ParcelConstructor
	private GameState(int money, int lemonade, GameGroceries inventory, GameGroceries prices) {
		this.money = money;
		this.lemonade = lemonade;
		this.inventory = inventory;
		this.prices = prices;
	}

	GameState(GameModel model) {
		inventory = model.inventory;
		prices = model.prices;
		money = model.money;
		lemonade = model.lemonade;
	}

	@Override
	public int getMoney() {
		return money;
	}

	@Override
	public int getLemonade() { return lemonade; }

	@Override
	public GameGroceries getPrices() {
		return prices;
	}

	@Override
	public GameGroceries getInventory() {
		return inventory;
	}
}

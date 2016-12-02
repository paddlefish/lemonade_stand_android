package net.paddlefish.lemonadestand.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class GameState extends GameModelBase implements IGameState, Parcelable {
	private final GameGroceries inventory;
	private final GameGroceries prices;
	private final int money;
	private final int lemonade;

	GameState(GameModel model) {
		inventory = model.inventory;
		prices = model.prices;
		money = model.money;
		lemonade = model.lemonade;
	}

	Creator<GameState> CREATOR;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeParcelable(inventory, 0);
		parcel.writeParcelable(prices, 0);
		parcel.writeInt(money);
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

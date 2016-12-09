package net.paddlefish.lemonadestand.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class GameState extends GameModelBase implements IGameState, Parcelable {
	private final GameGroceries inventory;
	private final GameGroceries prices;
	private final int money;
	private final int lemonade;

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

	static Creator<GameState> CREATOR = new Parcelable.Creator<GameState>() {
		@Override
		public GameState createFromParcel(Parcel parcel) {
			return new GameState(
					parcel.readInt(),
					parcel.readInt(),
					(GameGroceries) parcel.readParcelable(GameGroceries.class.getClassLoader()),
					(GameGroceries) parcel.readParcelable(GameGroceries.class.getClassLoader())
			);
		}

		@Override
		public GameState[] newArray(int i) {
			return new GameState[i];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		// The order here must match the order in the CREATOR createFromParcel, above.
		// With a runnable of some kind and a clever bit of templating it'd be possible
		// to describe these in a single helper fn...
		parcel.writeInt(money);
		parcel.writeInt(lemonade);
		parcel.writeParcelable(inventory, 0);
		parcel.writeParcelable(prices, 0);
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

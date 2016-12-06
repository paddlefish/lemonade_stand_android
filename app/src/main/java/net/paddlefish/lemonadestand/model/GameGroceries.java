package net.paddlefish.lemonadestand.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class GameGroceries implements Parcelable {
	private final int lemons;
	private final int sugar;
	private final int ice;

	public GameGroceries(int lemons, int sugar, int ice) {
		this.lemons = lemons;
		this.sugar = sugar;
		this.ice = ice;
	}

	Creator<GameGroceries> CREATOR;
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(lemons);
		parcel.writeInt(sugar);
		parcel.writeInt(ice);
	}

	public int getIce() {
		return ice;
	}

	public int getSugar() {
		return sugar;
	}

	public int getLemons() {
		return lemons;
	}
}


package net.paddlefish.lemonadestand.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.parceler.ParcelConstructor;

@org.parceler.Parcel
public final class GameGroceries {
	final int lemons;
	final int sugar;
	final int ice;

	@ParcelConstructor
	public GameGroceries(int lemons, int sugar, int ice) {
		this.lemons = lemons;
		this.sugar = sugar;
		this.ice = ice;
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


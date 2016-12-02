package net.paddlefish.lemonadestand;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.paddlefish.lemonadestand.model.GameState;

/**
 * Base class for any fragment that displays game state.
 * Created by arahn on 12/1/16.
 */

public class GameStateFragment extends Fragment {

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	protected static final String ARG_GAME_STATE = "gameState";

	protected GameState mGameState;

	public void setArguments(GameState gameState) {
		Bundle args = new Bundle();
		args.putParcelable(ARG_GAME_STATE, gameState);
		setArguments(args);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mGameState = getArguments().getParcelable(ARG_GAME_STATE);
		}
	}
}

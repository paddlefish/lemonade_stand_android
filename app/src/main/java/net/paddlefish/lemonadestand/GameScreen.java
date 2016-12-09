package net.paddlefish.lemonadestand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jgarrison2 on 12/9/2016.
 */
enum GameScreen {
	HELLO ("hello"),
	MONEY ("money"),
	SHOPPING ("shopping"),
	MAKING ("making"),
	DONE_SELLING ("done_selling"),
	OUT_OF_MONEY ("out_of_money");

	final String screenCode;
	private static final Map<String, GameScreen> ELEMENTS;
	GameScreen(String screenCode) {
		this.screenCode = screenCode;
	}

	static {
		Map<String, GameScreen> elements = new HashMap<>();
		for (GameScreen value : values()) {
			elements.put(value.screenCode, value);
		}
		ELEMENTS = Collections.unmodifiableMap(elements);
	}

	public static GameScreen screenForCode(String screenCode) {
		GameScreen result = ELEMENTS.get(screenCode);
		if (result != null) {
			return result;
		}
		return HELLO;
	}
}

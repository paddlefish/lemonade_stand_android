package net.paddlefish.lemonadestand.model;

/**
 * Server model object for game theme with details.
 *
 * Created by arahn on 12/12/16.
 */

public class GameThemeDetails extends GameTheme {
	public String url;
	public String info;
	public long rgb;

	public GameThemeDetails() {}

	public GameThemeDetails(String name, String color, String url, String info, long rgb) {
		super(name, color);
		this.url = url;
		this.info = info;
		this.rgb = rgb;
	}
}

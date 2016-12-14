package net.paddlefish.lemonadestand.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import net.paddlefish.lemonadestand.model.GameTheme;
import net.paddlefish.lemonadestand.model.GameThemeDetails;

/**
 * For storing and restoring selected theme.
 *
 * Created by arahn on 12/13/16.
 */
public class Preferences {

	private SharedPreferences settings;
	private static Preferences sharedInstance;

	public static void initPrefs(Context context) {
		sharedInstance = new Preferences();
		sharedInstance.settings = context.getSharedPreferences("Lemonade Stand", Context.MODE_PRIVATE);
	}

	public static boolean isSelectedTheme(GameTheme theme) {
		return isSelectedTheme(theme.color);
	}

	public static boolean isSelectedTheme(GameThemeDetails theme) {
		return isSelectedTheme(theme.color);
	}

	public static boolean isSelectedTheme(String color) {
		String selectedTheme = sharedInstance.settings.getString("theme", "yellow");
		return selectedTheme.equals(color);
	}

	public static void saveTheme(GameThemeDetails theme) {
		SharedPreferences.Editor editor = sharedInstance.settings.edit();
		editor.putString("theme", theme.color);
		editor.putLong("theme_rgb", theme.rgb);
		editor.apply();
	}
}

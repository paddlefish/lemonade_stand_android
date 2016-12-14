package net.paddlefish.lemonadestand.service;

import net.paddlefish.lemonadestand.model.GameTheme;
import net.paddlefish.lemonadestand.model.GameThemeDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A simple HTTP based service that uses Retrofit
 *
 * Created by arahn on 12/12/16.
 */

public interface ThemeService {

	@GET("api/v1/themes.json")
	Call<List<GameTheme>> getThemes();

	@GET("api/v1/theme/{color}/theme.json")
	Call<GameThemeDetails> getTheme(@Path("color") String color);
}

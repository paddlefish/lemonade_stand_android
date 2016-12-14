package net.paddlefish.lemonadestand;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.paddlefish.lemonadestand.model.GameTheme;
import net.paddlefish.lemonadestand.model.GameThemeDetails;
import net.paddlefish.lemonadestand.service.ThemeService;
import net.paddlefish.lemonadestand.theme.ThemeAdapter;
import net.paddlefish.lemonadestand.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.JacksonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ThemesActivity extends AppCompatActivity {
	ThemeService mThemeService;
	ThemeAdapter mThemeAdapter;
	ListView mThemeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_themes);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://paddlefish.net/evine_training/")
				.addConverterFactory(JacksonConverterFactory.create())
				.build();

		mThemeList = (ListView) findViewById(R.id.themeList);

		mThemeAdapter = new ThemeAdapter(ThemesActivity.this);
		mThemeList.setAdapter(mThemeAdapter);

		mThemeService = retrofit.create(ThemeService.class);
		mThemeService.getThemes().enqueue(new GetThemeListCallback());
		mThemeList.setOnItemClickListener(new ThemeClickListener());
	}

	public class ThemeClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			GameThemeDetails theme = (GameThemeDetails) adapterView.getAdapter().getItem(i);
			Preferences.saveTheme(theme);
			mThemeAdapter.notifyDataSetChanged();
		}
	}

	private void setThemes(List<GameTheme> themes) {
		ArrayList<GameThemeDetails> items = new ArrayList<>(themes.size());
		for (GameTheme theme: themes) {
			GameThemeDetails themeDetails = new GameThemeDetails(theme.name, theme.color, null, null, Color.YELLOW);
			items.add(themeDetails);
			requestThemeDetails(theme);
		}

		mThemeAdapter.addAll(items);
	}

	private void requestThemeDetails(GameTheme theme) {
		mThemeService.getTheme(theme.color).enqueue(new GameThemeDetailsCallback());
	}

	private void updateTheme(GameThemeDetails newItem) {
		final int count = mThemeAdapter.getCount();
		GameThemeDetails oldItem = null;
		int position = 0;
		for (int i = 0; i < count; i++) {
			final GameThemeDetails item = mThemeAdapter.getItem(i);
			if (item == null) {
				break;
			}
			if (item.color.equals(newItem.color)) {
				position = i;
				oldItem = item;
				break;
			}
		}
		if (oldItem != null) {
			mThemeAdapter.remove(oldItem);
		}
		mThemeAdapter.insert(newItem, position);
		mThemeAdapter.notifyDataSetChanged();
	}

	private class GetThemeListCallback implements Callback<List<GameTheme>> {

		@Override
		public void onResponse(Response<List<GameTheme>> response) {
          setThemes(response.body());
		}

		@Override
		public void onFailure(Throwable t) {
            Log.e("ThemesActivity", "Can't get details", t);
		}
    }

    private class GameThemeDetailsCallback implements Callback<GameThemeDetails> {

        @Override
        public void onResponse(Response<GameThemeDetails> response) {
            updateTheme(response.body());
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e("ThemesActivity", "Can't get details", t);
        }
    }
}

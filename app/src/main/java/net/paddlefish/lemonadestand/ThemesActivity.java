package net.paddlefish.lemonadestand;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ThemesActivity extends AppCompatActivity {
	ThemeService mThemeService;
	ThemeAdapter mThemeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_themes);
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
}

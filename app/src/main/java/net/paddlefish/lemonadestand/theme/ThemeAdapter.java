package net.paddlefish.lemonadestand.theme;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.paddlefish.lemonadestand.R;
import net.paddlefish.lemonadestand.model.GameThemeDetails;
import net.paddlefish.lemonadestand.utils.Preferences;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * An adapter for driving a ListView of GameThemeDetails
 *
 * Created by arahn on 12/13/16.
 */
public class ThemeAdapter extends ArrayAdapter<GameThemeDetails> {
	public ThemeAdapter(Activity activity) {
		super(activity, R.layout.theme_item);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.theme_item, parent, false);
		}
		GameThemeDetails theme = getItem(position);
		if (theme != null) {
			ImageView iconView = (ImageView) view.findViewById(R.id.themeIcon);
			String url = theme.url;

			// FIXME : Use Glide to load that url into iconView

			TextView themeInfoView = (TextView) view.findViewById(R.id.themeInfo);
			themeInfoView.setText(theme.info);

			TextView themeNameView = (TextView) view.findViewById(R.id.themeName);
			themeNameView.setText(theme.name);

			ImageView checkedView = (ImageView) view.findViewById(R.id.themeChecked);
			Boolean isCurrentTheme = Preferences.isSelectedTheme(theme);
			if (isCurrentTheme) {
				checkedView.setVisibility(VISIBLE);
			} else {
				checkedView.setVisibility(GONE);
			}
		}
		return view;
	}


}

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

import butterknife.ButterKnife;

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
		ThemeViewHolder view = (ThemeViewHolder) convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = (ThemeViewHolder) inflater.inflate(R.layout.theme_item, parent, false);
			view.bindViews();
		}
		GameThemeDetails theme = getItem(position);
		if (theme != null) {
			ImageView iconView = view.mIconView;
			String url = theme.url;

			// FIXME : Use Glide to load that url into iconView
			Glide.with(getContext()).load(url).into(iconView);

			TextView themeInfoView = view.mInfoView;
			themeInfoView.setText(theme.info);

			TextView themeNameView = view.mNameView;
			themeNameView.setText(theme.name);

			ImageView checkedView = view.mCheckedView;
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

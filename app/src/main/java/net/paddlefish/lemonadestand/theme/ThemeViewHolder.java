package net.paddlefish.lemonadestand.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.paddlefish.lemonadestand.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Simple ViewHolder for Theme ListView
 *
 * Created by arahn on 12/14/16.
 */
public class ThemeViewHolder extends LinearLayout {
	@BindView(R.id.themeIcon)
	ImageView mIconView;
	@BindView(R.id.themeInfo)
	TextView mInfoView;
	@BindView(R.id.themeName)
	TextView mNameView;

	ImageView mCheckedView;

	public ThemeViewHolder(Context context) {
		super(context);
	}

	public ThemeViewHolder(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	void bindViews() {
		ButterKnife.bind(this);

		// FIXME: Use butterknife to bind mCheckedView to R.id.themeChecked
		// Instead of this code:
		// Delete from here...
		mCheckedView = (ImageView) findViewById(R.id.themeChecked);

		// ... to here and replace with your implementation!
	}
}

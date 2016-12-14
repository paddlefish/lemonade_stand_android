package net.paddlefish.lemonadestand;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesActivity extends AppCompatActivity {

	static class Section {
		public String section_title;
		public String[] rules;

		Section() {}

		Section(String section_title, String[] rules) {
			this.section_title = section_title;
			this.rules = rules;
		}
	}

	/**
	 * Provides a list of rules, ready to be fed into SimpleAdapter
	 * Each entry in the list has these "from" keys
	 * "rule" -- the rule written out.
	 * "index" -- a string containing the rule number (starting with 1)
	 * "icon" -- a java.lang.Number containing the integer resource id of the icon
	 *
	 * @return List of entries for SimpleAdapter
	 */
	private List<Map<String, Object>> getSimpleRules() {
		String [] rules = {
			"No crying allowed",
			"When live gives you lemons, make lemonade!",
			"Sell lemonade for a higher price than it cost you to make it",
			"Try to earn $100 to win!",
		};
		ArrayList list = new ArrayList<>();
		for(int i = 0 ; i < rules.length; i++) {
			String rule = rules[i];
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("rule", rule);
			map.put("index", String.format("%d", i+1));
			map.put("icon", R.drawable.blinky);
			list.add(map);
		}
		return list;
	}

	/**
	 * Provides a list of mSections, ready to be fed into our RulesAdapter
	 *
	 * @return List of entries for RulesAdapter
	 */
	private Section[] getRules() {
		try {
			InputStream is = getAssets().open("rules.json");
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(is, Section[].class);
		}
		catch(IOException ioe) {
			String[] rules = new String[] { ioe.getMessage() };
			return new Section[] { new Section("Error", rules) };
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar it = getSupportActionBar();
		if (it != null) {
			it.setDisplayHomeAsUpEnabled(true);
		}

		RecyclerView rulesList = (RecyclerView) findViewById(R.id.rulesList);
		rulesList.setAdapter(new RulesAdapter(getRules(), this));
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		rulesList.setLayoutManager(layoutManager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				supportFinishAfterTransition();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	static abstract class RulesViewHolder extends RecyclerView.ViewHolder {
		RulesViewHolder(View view) {
			super(view);
		}
		abstract void bindView(Section section, int row);
	}

	static class RulesItemViewHolder extends RulesViewHolder {
		final TextView mTextView;
		final ImageView mImageView;

		RulesItemViewHolder(View view) {
			super(view);
			mTextView = (TextView) view.findViewById(R.id.rulesListRuleText);
			mImageView = (ImageView) view.findViewById(R.id.rulesListIcon);
		}
		void bindView(Section section, int row) {
			mTextView.setText(section.rules[row]);
			mImageView.setImageResource(R.drawable.ic_lemon_top);
			SharedPreferences settings = mImageView.getContext().getSharedPreferences("Lemonade Stand", MODE_PRIVATE);
			int tint = (int) settings.getLong("theme_rgb", Color.YELLOW);
			mImageView.setColorFilter(tint);
		}
	}

	static class RulesHeaderViewHolder extends RulesViewHolder {
		final TextView mTextView;
		RulesHeaderViewHolder(View view) {
			super(view);
			mTextView = (TextView) view.findViewById(R.id.rules_header);
		}
		void bindView(Section section, int row) {
			mTextView.setText(section.section_title);
		}
	}

	// SectionedRecyclerViewAdapter might be easier...
	// https://github.com/luizgrp/SectionedRecyclerViewAdapter
	static class RulesAdapter extends RecyclerView.Adapter<RulesViewHolder> {
		private final static int HEADER_VIEW_TYPE = 1;
		private final static int ROW_VIEW_TYPE = 2;

		private final Section[] mSections;
		private final Context mContext;

		RulesAdapter(Section[] sections, Context context) {
			this.mSections = sections;
			this.mContext = context;
		}

		@Override
		public int getItemViewType(int position) {
			int sectionAndRow[] = sectionAndRowForPosition(position);
			if (sectionAndRow != null) {
				int row = sectionAndRow[1];
				if (row == -1) {
					return HEADER_VIEW_TYPE;
				}
			}
			return ROW_VIEW_TYPE;
		}

		@Override
		public int getItemCount() {
			int total = mSections.length;
			for (Section mSection : mSections) {
				total += mSection.rules.length;
			}
			return total;
		}

		@Override
		public RulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view;
			switch (viewType) {
				case HEADER_VIEW_TYPE:
					view = inflater.inflate(R.layout.rules_header_layout, parent, false);
					return new RulesHeaderViewHolder(view);
				case ROW_VIEW_TYPE:
					view = inflater.inflate(R.layout.rules_item, parent, false);
					return new RulesItemViewHolder(view);
			}
			return null;
		}

		@Override
		public void onBindViewHolder(RulesViewHolder holder, int position) {
			int sectionAndRow[] = sectionAndRowForPosition(position);
			if (sectionAndRow != null) {
				Section section = mSections[sectionAndRow[0]];
				holder.bindView(section, sectionAndRow[1]);
			}
		}

		private @Nullable int[] sectionAndRowForPosition(int position) {
			for (int sectionIndex = 0; sectionIndex <= mSections.length; ++sectionIndex) {
				Section section = mSections[sectionIndex];
				if (position <= section.rules.length) {
					return new int[] { sectionIndex, position - 1 };
				}
				position -= section.rules.length + 1;
			}
			return null;
		}
	}

}

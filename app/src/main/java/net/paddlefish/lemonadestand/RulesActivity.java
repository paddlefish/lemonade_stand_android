package net.paddlefish.lemonadestand;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesActivity extends AppCompatActivity {

	/**
	 * Provides a list of rules, ready to be fed into SimpleAdapter
	 * Each entry in the list has these "from" keys
	 * "rule" -- the rule written out.
	 * "index" -- a string containing the rule number (starting with 1)
	 * "icon" -- a java.lang.Number containing the integer resource id of the icon
	 *
	 * @return List of entries for SimpleAdapter
	 */
	private List<Map<String, Object>> getRules() {
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
			map.put("icon", new Integer(R.drawable.blinky));
			list.add(map);
		}
		return list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// We need to wire up the R.id.rulesList and bind
		// the R.id.rulesListIcon and R.id.rulesListRuleText views to
		// the rules proided by getRules()

	}

}

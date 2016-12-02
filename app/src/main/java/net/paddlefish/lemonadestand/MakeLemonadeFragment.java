package net.paddlefish.lemonadestand;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;

import java.text.NumberFormat;
import java.text.ParseException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MakeLemonadeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeLemonadeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeLemonadeFragment extends GameStateFragment {

	private OnFragmentInteractionListener mListener;
	private int mPrice = 0;

	public MakeLemonadeFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param gameState The current state of the game.
	 * @return A new instance of fragment MakeLemonadeFragment.
	 */
	public static MakeLemonadeFragment newInstance(GameState gameState) {
		MakeLemonadeFragment fragment = new MakeLemonadeFragment();
		fragment.setArguments(gameState);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_make_lemonade, container, false);
		Button sellLemonadeButton = (Button) view.findViewById(R.id.startSellingNow);
		sellLemonadeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sellLemonadeButtonPressed();
			}
		});

		TextView glassesOfLemonadeView = (TextView) view.findViewById(R.id.numGlassesOfLemonadeLabel);
		Resources res = getResources();
		int lemonade = mGameState.getLemonade();
		String glassesString = res.getQuantityString(R.plurals.glasses_of_lemonade, lemonade, lemonade);
		glassesOfLemonadeView.setText(glassesString);

		int moneyRemaining = mGameState.getMoney();
		String remainingStr = res.getString(R.string.time_to_sell_lemonade);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		String remainingDollars = format.format(moneyRemaining / 100.0);
		remainingStr = String.format(remainingStr, remainingDollars);
		TextView remainingView = (TextView) view.findViewById(R.id.getCookingDetail);
		remainingView.setText(remainingStr);

		EditText priceEditText = (EditText) view.findViewById(R.id.whatToChargeInputView);
		priceEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				NumberFormat format = NumberFormat.getIntegerInstance();
				CharSequence text = textView.getText();
				try {
					mPrice = format.parse(text.toString()).intValue();
				}
				catch(ParseException e) {}
				return false;
			}
		});

		return view;
	}

	public void sellLemonadeButtonPressed() {
		if (mListener != null) {
			mListener.sellLemonade(mGameState, mPrice);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		void sellLemonade(IGameState gameState, int price);
	}
}

package net.paddlefish.lemonadestand;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoneSellingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoneSellingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneSellingFragment extends GameStateFragment {
	protected static final String ARG_NEW_EARNINGS_PARAM = "newEarnings";
	protected static final String ARG_WASTED_GLASSES_PARAM = "wastedGlasses";

	private OnFragmentInteractionListener mListener;
	private int mNewEarnings;
	private int mWastedGlassesOfLemonade;

	public DoneSellingFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param gameState The current state of the game.
	 * @return A new instance of fragment MakeLemonadeFragment.
	 */
	public static DoneSellingFragment newInstance(GameState gameState, int newEarnings, int wastedGlasses) {
		DoneSellingFragment fragment = new DoneSellingFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_GAME_STATE, gameState);
		args.putInt(ARG_NEW_EARNINGS_PARAM, newEarnings);
		args.putInt(ARG_WASTED_GLASSES_PARAM, wastedGlasses);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mNewEarnings = getArguments().getInt(ARG_NEW_EARNINGS_PARAM);
			mWastedGlassesOfLemonade = getArguments().getInt(ARG_WASTED_GLASSES_PARAM);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_done_selling, container, false);
		Button restUpButton = (Button) view.findViewById(R.id.doneSellingButton);

		TextView moneyEarnedView = (TextView) view.findViewById(R.id.doneSellingTitle);
		Resources res = getResources();
		NumberFormat format = NumberFormat.getCurrencyInstance();

		String dollarString = format.format((double) mNewEarnings / 100.0);
		String glassesString = String.format(res.getString(R.string.done_selling_title), dollarString);
		moneyEarnedView.setText(glassesString);

		int money = mGameState.getMoney();
		String newEarningsDollarString = format.format((double) money / 100.0);
		TextView newEarningsDetailsView = (TextView) view.findViewById(R.id.doneSellingDetails);
		if (money < 10000) {
			String newEarningsString = String.format(res.getString(R.string.done_selling_details), newEarningsDollarString, mWastedGlassesOfLemonade);
			newEarningsDetailsView.setText(newEarningsString);
			restUpButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					restUpButtonPressed();
				}
			});
		}
		else {
			String newEarningsString = String.format(res.getString(R.string.done_selling_you_won), newEarningsDollarString);
			newEarningsDetailsView.setText(newEarningsString);
			restUpButton.setText(R.string.play_again_button);
			restUpButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					restartGameButtonPushed();
				}
			});
		}

		return view;
	}

	public void restUpButtonPressed() {
		if (mListener != null) {
			mListener.restUp(mGameState);
		}
	}

	public void restartGameButtonPushed() {
		if (mListener != null) {
			mListener.restartGame();
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
		void restUp(IGameState gameState);
		void restartGame();
	}
}

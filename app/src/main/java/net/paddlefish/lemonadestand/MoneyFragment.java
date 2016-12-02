package net.paddlefish.lemonadestand;

import android.content.Context;
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
 * {@link MoneyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyFragment extends GameStateFragment {

	private OnFragmentInteractionListener mListener;

	public MoneyFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param gameState The current state of the game.
	 * @return A new instance of fragment MoneyFragment.
	 */
	public static MoneyFragment newInstance(GameState gameState) {
		MoneyFragment fragment = new MoneyFragment();
		fragment.setArguments(gameState);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_money, container, false);
		Button goShoppingButton = (Button) view.findViewById(R.id.goShoppingButton);
		goShoppingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goShoppingButtonPressed(view);
			}
		});

		TextView dollarView = (TextView) view.findViewById(R.id.bigDollarSign);
		NumberFormat format = NumberFormat.getCurrencyInstance();

		String dollarString = format.format((double) mGameState.getMoney() / 100.0);
		dollarView.setText(dollarString);

		return view;
	}

	public void goShoppingButtonPressed(View view) {
		if (mListener != null) {
			mListener.goShoppingButtonPressed(mGameState);
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
		void goShoppingButtonPressed(IGameState gameState);
	}
}

package net.paddlefish.lemonadestand;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.paddlefish.lemonadestand.model.Accessor;
import net.paddlefish.lemonadestand.model.GameGroceries;
import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.model.IGameState;

import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends GameStateFragment {
	final static String PARAM_QTY_ORDERED = "PARAM_QTY_ORDERED";

	private GameGroceries qtyOrdered = new GameGroceries(0, 0, 0);

	private OnFragmentInteractionListener mListener;

	public ShoppingFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param gameState The current state of the game.
	 * @return A new instance of fragment MoneyFragment.
	 */
	public static ShoppingFragment newInstance(GameState gameState) {
		ShoppingFragment fragment = new ShoppingFragment();
		fragment.setArguments(gameState);
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(PARAM_QTY_ORDERED, Parcels.wrap(qtyOrdered));

		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			GameGroceries oldQtyOrdered = savedInstanceState.getParcelable(PARAM_QTY_ORDERED);
			if (oldQtyOrdered != null) {
				qtyOrdered = oldQtyOrdered;
			}
		}
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_shopping, container, false);

		Button buyGroceriesButton = (Button) view.findViewById(R.id.buyGroceriesButton);
		buyGroceriesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				buyGroceriesButtonPressed(view);
			}
		});

		ListView shoppingList = (ListView) view.findViewById(R.id.groceryStoreItemsList);
		shoppingList.setAdapter(new ProductListAdaptor());

		return view;
	}


	public void buyGroceriesButtonPressed(View view) {
		if (mListener != null) {
			mListener.buyGroceriesButtonPressed(mGameState, qtyOrdered);
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
		void buyGroceriesButtonPressed(IGameState previousState, GameGroceries qtyOrdered);
	}

	class ProductListAdaptor extends BaseAdapter {
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public View getView(final int i, View view, ViewGroup parent) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 				view = inflater.inflate(R.layout.list_fragment_shopping, parent, false);
			}
			final TextView productName = (TextView) view.findViewById(R.id.shoppingListProductName);
			final TextView productDetails = (TextView) view.findViewById(R.id.shoppingListProductDetails);

			final Resources res = getResources();
			final NumberFormat format = NumberFormat.getCurrencyInstance();
			final Accessor accessor = Accessor.accessorForRow(i);
			productName.setText(accessor.productName());
			final double price = accessor.getPrice(mGameState) / 100.0;
			final String priceStr = format.format(price);
			final String details = String.format(res.getString(accessor.details()), priceStr);
			productDetails.setText(details);
			final int qty = accessor.get(qtyOrdered);

			final TextView qtyView = (TextView) view.findViewById(R.id.productQtyView);
			qtyView.setText(String.format(Locale.US, "%d", qty));

			addListenerToButton(view, accessor, qtyView, R.id.productAddButton, new AddOne());
			addListenerToButton(view, accessor, qtyView, R.id.productRemoveButton, new SubtractOne());

			final TextView onHandView = (TextView) view.findViewById(R.id.shoppingListProductQtyOnHand);
			String onHandString = String.format(getResources().getString(R.string.product_details_qty_on_hand), accessor.getInventory(mGameState));
			onHandView.setText(onHandString);

			return view;
		}

		private void addListenerToButton(View view, Accessor accessor, TextView qtyView, int buttonId, ArithmeticClickListener listener) {
			final Button addButton = (Button) view.findViewById(buttonId);
			listener.mAccessor = accessor;
			listener.mQtyView = qtyView;
			addButton.setOnClickListener(listener);
		}
	}
	abstract class ArithmeticClickListener implements View.OnClickListener {
		Accessor mAccessor;
		TextView mQtyView;

		abstract int op(int x);

		@Override
		public void onClick(View view) {
			final int qty = op(mAccessor.get(qtyOrdered));
			mQtyView.setText(String.format(Locale.US, "%d", qty));
			qtyOrdered = mAccessor.set(qtyOrdered, qty);
		}
	}
	class AddOne extends ArithmeticClickListener {
		public int op(int x) { return Math.min(9, x + 1); }
	}
	class SubtractOne extends ArithmeticClickListener {
		public int op(int x) { return Math.max(0, x - 1); }
	}
}

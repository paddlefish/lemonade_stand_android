package net.paddlefish.lemonadestand.model;

import net.paddlefish.lemonadestand.service.IPurchasingService;
import net.paddlefish.lemonadestand.service.ISellingService;
import net.paddlefish.lemonadestand.service.PurchasingService;
import net.paddlefish.lemonadestand.service.SellingService;
import net.paddlefish.lemonadestand.utils.Cancellable;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test basic game model functionality.
 *
 * Created by arahn on 12/1/16.
 */

public final class GameModelUnitTest {
	@Test
	public void money_initialValue() throws Exception {
		GameModel model = new GameModel(10);
		assertEquals(10, model.getMoney());
	}

	@Test
	public void prices_initialValue() throws Exception {
		GameModel model = new GameModel(10);
		GameGroceries initialPrices = model.getPrices();
		assertNotNull(initialPrices);
		assertTrue("Initial lemon prices is not zero", initialPrices.getLemons() > 0);
	}

	@Test
	public void prices_changeValue() throws Exception {
		GameModel model = new GameModel(10);
		GameGroceries initialPrices = model.getPrices();
		assertNotNull(initialPrices);
		int initialLemonPrice = initialPrices.getLemons();
		GameGroceries newPrices = new GameGroceries(initialPrices.getLemons() + 1, 1, 1);
		model.setPrices(newPrices);
		newPrices = model.getPrices();
		assertEquals(initialLemonPrice + 1, newPrices.getLemons());
	}

	@Test
	public void sell_sellWithNothing() throws Exception {
		final int initialMoney = 10;
		GameModel model = new GameModel(initialMoney);
		model.mSellingService = new TestSellingService(0);
		TestSalesResultsCallback callback = new TestSalesResultsCallback();
		model.sellLemonade(100, callback);
		assertEquals(initialMoney, model.getMoney());
	}

	private void buyTest_buyWithEnoughMoney(Accessor accessor) throws Exception {
		final int initialMoney = 100;
		GameModel model = new GameModel(initialMoney);
		GameGroceries myPrices = new GameGroceries(0, 0, 0);
		myPrices = accessor.set(myPrices, 10);
		model.setPrices(myPrices);
		assertEquals(0, accessor.get(model.getInventory()));
		GameGroceries toBuy = accessor.set(new GameGroceries(0, 0, 0), 10);
		model.mPurchasingService = new TestPurchasingService();
		model.buySome(toBuy, new IPurchasingService.IPurchaseCompletion() {
			@Override
			public void result(boolean success) {
				assertTrue(success);
			}
		});
		assertEquals(0, model.getMoney());
		assertEquals(10, accessor.get(model.getInventory()));
	}
	@Test
	public void buyLemons_buyWithEnoughMoney() throws Exception {
		buyTest_buyWithEnoughMoney(new Accessor.Lemon());
	}

	@Test
	public void buySugar_buyWithEnoughMoney() throws Exception {
		buyTest_buyWithEnoughMoney(new Accessor.Sugar());
	}

	@Test
	public void buyIce_buyWithEnoughMoney() throws Exception {
		buyTest_buyWithEnoughMoney(new Accessor.Ice());
	}

	@Test
	public void make_lemonade() throws Exception {
		final int initialMoney = 100;
		GameModel model = new GameModel(initialMoney);
		model.inventory = new GameGroceries(32, 32, 32);
		model.makeLemonade();
		assertEquals(128, model.lemonade);
		assertEquals(0, model.inventory.getLemons());
		assertEquals(16, model.inventory.getSugar());
		assertEquals(24, model.inventory.getIce());
	}

	@Test
	public void sell_lemonade() throws Exception {
		final int initialMoney = 100;
		GameModel model = new GameModel(initialMoney);
		model.mSellingService = new TestSellingService(10);
		model.lemonade = 16;
		TestSalesResultsCallback callback = new TestSalesResultsCallback();
		model.sellLemonade(99, callback);
		assertEquals(6, callback.mResults.glassesWasted);
		assertEquals(990, callback.mResults.moneyEarned);
		assertEquals(0, model.lemonade);
		assertEquals(0, model.inventory.getLemons());
		assertEquals(0, model.inventory.getSugar());
		assertEquals(0, model.inventory.getIce());
	}

	private static class TestSellingService implements ISellingService {
		final int mNumToSell;
		TestSellingService(int numToSell) {
			this.mNumToSell = numToSell;
		}
		@Override
		public Cancellable sellLemonade(GameState gameState, int price, SellingService.SellingCallback callback) {
			callback.done(mNumToSell);
			return new Cancellable() {
				@Override
				public boolean cancel() {
					return false;
				}
			};
		}
	}

	private static class TestSalesResultsCallback implements GameModel.SalesResults.Callback {
		GameModel.SalesResults mResults;
		@Override
		public void saleCompleted(GameModel.SalesResults results) {
			mResults = results;
		}

		@Override
		public void saleInProgress(int hour, int numSold) {

		}
	}

	private static class TestPurchasingService implements IPurchasingService {
		@Override
		public int calculatePriceForGroceries(GameGroceries quantities, GameGroceries prices) {
			return PurchasingService.defaultCalculatePriceForGroceries(quantities, prices);
		}

		@Override
		public void purchaseGroceries(GameGroceries quantities, int money, GameGroceries prices, IPurchaseCompletion completion) {
			completion.result(money == calculatePriceForGroceries(quantities, prices));
		}
	}
}

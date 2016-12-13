package net.paddlefish.lemonadestand.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test basic game model functionality.
 *
 * Created by arahn on 12/1/16.
 */

public final class GameModelUnitTest {
//	@Test
//	public void money_initialValue() throws Exception {
//		GameModel model = new GameModel(10);
//		assertEquals(10, model.getMoney());
//	}
//
//	@Test
//	public void prices_initialValue() throws Exception {
//		GameModel model = new GameModel(10);
//		GameGroceries initialPrices = model.getPrices();
//		assertNotNull(initialPrices);
//		assertTrue("Initial lemon prices is not zero", initialPrices.getLemons() > 0);
//	}
//
//	@Test
//	public void prices_changeValue() throws Exception {
//		GameModel model = new GameModel(10);
//		GameGroceries initialPrices = model.getPrices();
//		assertNotNull(initialPrices);
//		int initialLemonPrice = initialPrices.getLemons();
//		GameGroceries newPrices = new GameGroceries(initialPrices.getLemons() + 1, 1, 1);
//		model.setPrices(newPrices);
//		newPrices = model.getPrices();
//		assertEquals(initialLemonPrice + 1, newPrices.getLemons());
//	}
//
//	@Test
//	public void sell_sellWithNothing() throws Exception {
//		final int initialMoney = 10;
//		GameModel model = new GameModel(initialMoney);
//		model.sellLemonade(1.0, 100);
//		assertEquals(initialMoney, model.getMoney());
//	}
//
//	private void buyTest_buyWithEnoughMoney(Accessor accessor) throws Exception {
//		final int initialMoney = 100;
//		GameModel model = new GameModel(initialMoney);
//		GameGroceries myPrices = new GameGroceries(0, 0, 0);
//		myPrices = accessor.set(myPrices, 10);
//		model.setPrices(myPrices);
//		assertEquals(0, accessor.get(model.getInventory()));
//		GameGroceries toBuy = accessor.set(new GameGroceries(0, 0, 0), 10);
//		assertTrue(model.buySome(toBuy));
//		assertEquals(0, model.getMoney());
//		assertEquals(10, accessor.get(model.getInventory()));
//	}
//	@Test
//	public void buyLemons_buyWithEnoughMoney() throws Exception {
//		buyTest_buyWithEnoughMoney(new Accessor.Lemon());
//	}
//
//	@Test
//	public void buySugar_buyWithEnoughMoney() throws Exception {
//		buyTest_buyWithEnoughMoney(new Accessor.Sugar());
//	}
//
//	@Test
//	public void buyIce_buyWithEnoughMoney() throws Exception {
//		buyTest_buyWithEnoughMoney(new Accessor.Ice());
//	}
//
//	@Test
//	public void make_lemonade() throws Exception {
//		final int initialMoney = 100;
//		GameModel model = new GameModel(initialMoney);
//		model.inventory = new GameGroceries(32, 32, 32);
//		model.makeLemonade();
//		assertEquals(128, model.lemonade);
//		assertEquals(0, model.inventory.getLemons());
//		assertEquals(16, model.inventory.getSugar());
//		assertEquals(24, model.inventory.getIce());
//	}
//
//	@Test
//	public void sell_lemonade() throws Exception {
//		final int initialMoney = 100;
//		GameModel model = new GameModel(initialMoney);
//		model.lemonade = 16;
//		model.sellLemonade(1.0, 99);
//		assertEquals(0, model.lemonade);
//		assertEquals(0, model.inventory.getLemons());
//		assertEquals(0, model.inventory.getSugar());
//		assertEquals(0, model.inventory.getIce());
//		assertEquals(16 * 99 + 100, model.money);
//	}
}

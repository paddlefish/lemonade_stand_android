package net.paddlefish.lemonadestand.service;

import net.paddlefish.lemonadestand.model.GameState;
import net.paddlefish.lemonadestand.utils.Cancellable;

/**
 * Abstraction around SellingService, primarily for Testing
 */
public interface ISellingService {
	Cancellable sellLemonade(GameState gameState, int price, final SellingService.SellingCallback callback);
}

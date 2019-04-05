package com.stocks.ui;

import java.util.List;

import com.stocks.domain.Stock;

/**
 * A common interface for MainView page in UI. 
 * Following the MVP pattern, this interface can be used in {@link MainViewPresenter}.  
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
public interface MainView {
	/**
	 * Reloads the stocks grid.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param stocks
	 */
	public void reloadStocksGrid(List<Stock> stocks);
}

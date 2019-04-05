package com.stocks.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.stocks.domain.Stock;
import com.stocks.services.EventType;
import com.stocks.services.StockService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Following the MVP pattern, this class separates the required logic from UI
 * components of a {@link MainView}.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Component
@Data
@Scope(value = "session")
@Slf4j
public class MainViewPresenter {
	@Autowired
	private StockService stockService;
	private MainView mainView;

	/**
	 * Reads stock from database and passes to be load on the grid.
	 * 
	 * @author Jalal
	 * @since 20190403
	 */
	public void reloadStocksList() {
		//TODO: Caching can be used to avoid DB calls overhead.
		List<Stock> stocks = stockService.getAllStocks();
		mainView.reloadStocksGrid(stocks);
	}

	/**
	 * Takes care of the logic related to event pushes.
	 * 
	 * @author Jalal
	 * @since 20190403
	 */
	public void handlePushEvent(EventType event) {
		log.info("Event occurred: {}", event, this);
		switch (event) {
		case StocksListChanged:
			reloadStocksList();
			break;
		default:
			log.error("Event not found: {}", event, this);
		}
	}

}

package com.stocks.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.domain.Stock;
import com.stocks.exceptions.StockNotFoundException;
import com.stocks.repositories.StockRepository;

/**
 * This class provides and implementation for {@link StockService}. It uses
 * {@link StockRepository} to communicate with DB.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Service
public class StockServiceImpl implements StockService {
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private NotificationService notificationService;

	/**
	 * Adds a given stock to the DB and returns the {@link Stock}.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param stock
	 * @return Stock
	 */
	@Override
	public Stock addStock(Stock stock) {
		Stock stockFromDB = stockRepository.save(stock);
		
		notificationService.notifyEvent(EventType.StocksListChanged);
		
		return stockFromDB;
	}

	/**
	 * Updates a given stock's current price and returns the {@link Stock}.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param id
	 * @param price
	 * @return Stock
	 */
	@Override
	public Stock updateStockPrice(Long id, Double price) throws StockNotFoundException {
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException());
		stock.setCurrentPrice(price);
		stock.setLastUpdateTimeStamp(new Date());
		Stock stockFromDB = stockRepository.save(stock);

		notificationService.notifyEvent(EventType.StocksListChanged);

		return stockFromDB;

	}

	/**
	 * returns a {@link Stock} given the id.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param id
	 * @return Stock
	 */
	@Override
	public Stock getStock(Long id) throws StockNotFoundException {
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException());
		return stock;
	}

	/**
	 * Returns the list of all {@link Stock} from DB.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @return List<Stock>
	 */
	@Override
	public List<Stock> getAllStocks() {
		List<Stock> stocks = new ArrayList<>();

		for (Stock stock : stockRepository.findAll()) {
			stocks.add(stock);
		}
		return stocks;
	}

}

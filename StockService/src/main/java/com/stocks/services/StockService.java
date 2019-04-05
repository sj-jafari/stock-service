package com.stocks.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.domain.Stock;
import com.stocks.exceptions.StockNotFoundException;

/**
 * A general interface to define services available for {@link Stock}.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Service
public interface StockService {
	/**
	 * Adds a given stock to the DB and returns the {@link Stock}.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param stock
	 * @return Stock
	 */
	public Stock addStock(Stock stock);

	//TODO: update stock's basic info. to be implemented
	// public Stock updateStock(Stock stock);
	
	/**
	 * Updates a given stock's current price and returns the {@link Stock}.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param id
	 * @param price
	 * @return Stock
	 */
	public Stock updateStockPrice(Long id, Double price) throws StockNotFoundException;

	/**
	 * returns a {@link Stock} given the id.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param id
	 * @return Stock
	 */
	public Stock getStock(Long id) throws StockNotFoundException;

	/**
	 * Returns the list of all {@link Stock} from DB.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @return List<Stock>
	 */
	public List<Stock> getAllStocks();
}

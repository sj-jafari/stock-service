package com.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.stocks.domain.Stock;
import com.stocks.exceptions.StockNotFoundException;
import com.stocks.services.StockService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTests {
	@Autowired
	private StockService stockService;
	private List<Stock> initialStocksList;

	@Before
	public void init() {
		initialStocksList = stockService.getAllStocks();
	}

	@Test
	public void contextLoads() {
		assertNotNull(stockService);
		assertNotNull(initialStocksList);
		assertNotEquals(0, initialStocksList.size());
	}

	// -- Create Stock tests
	@Test
	public void createStock() {
		int numberOfStocksBeforeInsert = stockService.getAllStocks().size();
		Stock stock = new Stock();
		stock.setSymbolName("TestSymbol");
		stock.setCurrentPrice(100.0);
		Stock stockFromDB = stockService.addStock(stock);
		int numberOfStocksAfterInsert = stockService.getAllStocks().size();
		assertNotNull(stockFromDB);
		assertEquals(stockFromDB.getSymbolName(), stock.getSymbolName());
		assertEquals(stockFromDB.getCurrentPrice(), stock.getCurrentPrice());
		assertNotNull(stockFromDB.getId());
		assertNotNull(stockFromDB.getLastUpdateTimeStamp());
		assertEquals(numberOfStocksBeforeInsert + 1, numberOfStocksAfterInsert);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void createStockNotUniqueSymbol() {
		Stock stock1 = new Stock();
		stock1.setSymbolName("NonUniqueSymbol");
		stock1.setCurrentPrice(2.0);

		Stock stock2 = new Stock();
		stock2.setSymbolName("NonUniqueSymbol");
		stock2.setCurrentPrice(2.0);

		stockService.addStock(stock1);
		stockService.addStock(stock2);
	}

	// -- Get Stocks tests
	@Test
	public void getAllStocks() {
		List<Stock> stocks = stockService.getAllStocks();
		assertNotNull(stocks);
		assertNotEquals(0, stocks.size());
	}

	@Test
	public void getStockById() throws StockNotFoundException {
		Long idToFind = initialStocksList.get(0).getId();
		Stock stock = stockService.getStock(idToFind);
		assertNotNull(stock);
		assertEquals(idToFind, stock.getId());
	}

	@Test(expected = StockNotFoundException.class)
	public void getStockByInvalidId() throws StockNotFoundException {
		Long idToFind = (long) -1;
		stockService.getStock(idToFind);
	}

	// -- Update Stock tests
	@Test
	public void updateStockPrice() throws StockNotFoundException {
		Long idToFind = initialStocksList.get(0).getId();
		Stock stock = stockService.getStock(idToFind);
		Double priceBeforeUpdate = stock.getCurrentPrice();

		stockService.updateStockPrice(idToFind, priceBeforeUpdate + 10);

		Stock stockFromDB = stockService.getStock(idToFind);
		Double priceAfterUpdate = stockFromDB.getCurrentPrice();

		assertEquals(Double.valueOf(priceBeforeUpdate + 10), priceAfterUpdate);
	}

	@Test(expected=Exception.class)
	public void updateStockPriceNegativePrice() throws StockNotFoundException {
		Long idToFind = initialStocksList.get(0).getId();
		stockService.updateStockPrice(idToFind, -10.0);

	}

	@Test(expected = StockNotFoundException.class)
	public void updateStockByInvalidId() throws StockNotFoundException {
		Long idToFind = (long) -1;
		stockService.updateStockPrice(idToFind, 10.0);
	}

}

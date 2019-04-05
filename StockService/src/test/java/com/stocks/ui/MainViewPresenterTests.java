package com.stocks.ui;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stocks.domain.Stock;
import com.stocks.services.EventType;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainViewPresenterTests implements MainView{
	@Autowired
	private MainViewPresenter mainViewPresenter; 
	
	@Before
	public void init() {
		mainViewPresenter.setMainView(this);
	}
	
	
	@Test
	public void reloadStocksListTest() {
		mainViewPresenter.reloadStocksList();
	}
	
	@Test
	public void handleStocksListChangedEventTest() {
		EventType event = EventType.StocksListChanged;
		mainViewPresenter.handlePushEvent(event);
	}
	
	
	@Override
	public void reloadStocksGrid(List<Stock> stocks) {
		assertNotNull(stocks);
		assertNotEquals(0, stocks.size());
	}

}

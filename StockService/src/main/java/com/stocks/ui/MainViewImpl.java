package com.stocks.ui;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.stocks.domain.Stock;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import lombok.extern.slf4j.Slf4j;

/**
 * The UI page implementation for Main View. The main purpose of this page is to
 * show a list of stocks. As well as the Reload button, changes made to stocks
 * data from anywhere are pushed to this page.
 * 
 * @author Jalal
 * @since 20190403
 */
@Route(value = "")
@Push
@Slf4j
public class MainViewImpl extends VerticalLayout implements MainView {
	private static final long serialVersionUID = 1L;
	private Registration broadcasterRegistration;;

	private Grid<Stock> stocksGrid;
	private Button reloadButton;

	@Autowired
	private transient MainViewPresenter mainViewPresenter;

	/**
	 * The constructor takes care of component creation.
	 * 
	 * @author Jalal
	 * @since 20190403
	 */
	public MainViewImpl() {
		// stocks grid
		stocksGrid = new Grid<>(Stock.class);
		stocksGrid.setColumnReorderingAllowed(true);
		stocksGrid.setColumns("id", "symbolName", "currentPrice", "lastUpdateTimeStamp");

		// refresh button
		reloadButton = new Button("Reload");
		setHorizontalComponentAlignment(Alignment.CENTER, reloadButton);
		reloadButton.setIcon(new Icon(VaadinIcon.REFRESH));
		reloadButton.addClickListener(event -> {
			mainViewPresenter.reloadStocksList();
		});

		// add components
		add(stocksGrid, reloadButton);

	}

	/**
	 * Initializes the data and the presenter class.
	 * 
	 * @author Jalal
	 * @since 20190403
	 */
	@PostConstruct
	private void loadInitData() {
		mainViewPresenter.setMainView(this);
		mainViewPresenter.reloadStocksList();
	}

	/**
	 * Given a list of stocks, reloads the grid. This method is generally called by
	 * the {@link MainViewPresenter}.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param stocks
	 */
	@Override
	public void reloadStocksGrid(List<Stock> stocks) {
		// log.info("reloading from presenter");
		stocksGrid.setItems(stocks);
	}

	// -- Push related methods ---//

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		broadcasterRegistration = PushNotifciationBroadcaster.register(newEvent -> {
			ui.access(() -> mainViewPresenter.handlePushEvent(newEvent));
		});
		log.info("registered to broadcaster: {}", attachEvent);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
		log.info("unregistered from broadcaster: {}", detachEvent);
	}
}

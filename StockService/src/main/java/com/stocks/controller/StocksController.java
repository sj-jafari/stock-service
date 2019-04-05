package com.stocks.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.authentication.Authenticator;
import com.stocks.authentication.LoginInfo;
import com.stocks.domain.Stock;
import com.stocks.exceptions.InvalidCredentialsException;
import com.stocks.exceptions.InvalidTokenException;
import com.stocks.exceptions.StockNotFoundException;
import com.stocks.services.StockService;

@RestController
@RequestMapping("api/v1")
public class StocksController {
	@Autowired
	private StockService stockService;
	@Autowired
	private Authenticator authenticator;

	@GetMapping("/hello")
	public ResponseEntity<?> getHelloMessage() {
		String message = "Hello!";
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginInfo loginInfo) throws InvalidCredentialsException {

		String token = authenticator.login(loginInfo.getUsername(), loginInfo.getPassword());
		JSONObject message = new JSONObject();
		message.put("token", token);
		return new ResponseEntity<String>(message.toString(), HttpStatus.OK);
	}

	@PostMapping(path = "/stock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createStock(@RequestBody Stock stock, @RequestHeader(value = "token") String token)
			throws InvalidTokenException {
		authenticator.isTokenValid(token);
		Stock stockFromDB = stockService.addStock(stock);
		return new ResponseEntity<Stock>(stockFromDB, HttpStatus.CREATED);
	}

	@GetMapping(path = "/stock", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllStocks() {
		// no need to authenticate here. this is a public service.
		List<Stock> stocksList = stockService.getAllStocks();
		return new ResponseEntity<List<Stock>>(stocksList, HttpStatus.OK);
	}

	@GetMapping(path = "/stock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStockById(@PathVariable Long id) throws StockNotFoundException {
		// no need to authenticate here. this is a public service.
		Stock stock = stockService.getStock(id);
		return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	}

	@PutMapping(path = "/stock/{id}/price", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateStockById(@PathVariable Long id, @RequestBody Double price,
			@RequestHeader(value = "token") String token) throws StockNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		Stock stock = stockService.updateStockPrice(id, price);
		return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	}
}

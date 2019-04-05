package com.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.stocks.controller.StocksController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockServiceApplication.class)
@AutoConfigureMockMvc
public class IntegrationTests {
	@Autowired
	private StocksController stocksController;
	private JSONArray initialStocksJsonArray;
	private String token;
	
	@Autowired
	private MockMvc mockMvc;

	@Before
	public void initialize() throws Exception {
		String loginInfo = "{\n" + "    \"username\": \"admin\",\n" + "    \"password\": \"admin\"\n" + "}";

		MvcResult loginResult = 
				mockMvc.perform(post("/api/v1/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(loginInfo)
						.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andReturn();
		token = new JSONObject(loginResult.getResponse().getContentAsString()).getString("token");
		
		MvcResult result = 
				mockMvc.perform(get("/api/v1/stock"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		initialStocksJsonArray = new JSONArray(result.getResponse().getContentAsString());
		
		assertNotNull(initialStocksJsonArray);
		assertNotEquals(0, initialStocksJsonArray.length());
	}

	@Test
	public void contextLoads() {
		assertNotNull(stocksController);
		assertNotNull(mockMvc);
	}
	
	@Test
	public void helloMessageTest() throws Exception {
		MvcResult result = 
				mockMvc.perform(get("/api/v1/hello"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		assertEquals("Hello!", result.getResponse().getContentAsString());
	}
	
	// ---- LOGIN
	@Test
	public void loginWithCorrectCredentials() throws Exception {
		String loginInfo = "{\n" + "    \"username\": \"admin\",\n" + "    \"password\": \"admin\"\n" + "}";

		MvcResult result = 
				mockMvc.perform(post("/api/v1/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(loginInfo)
						.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andReturn();
		token = new JSONObject(result.getResponse().getContentAsString()).getString("token");

		assertNotNull(token);
		assertNotEquals(0, token.length());
	}
	
	@Test
	public void loginWithIncorrectCredentials() throws Exception {
		String loginInfo = "{\n" + "    \"username\": \"xyz\",\n" + "    \"password\": \"xyz\"\n" + "}";

		mockMvc.perform(post("/api/v1/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginInfo)
				.characterEncoding("utf-8"))
		.andExpect(status().isUnauthorized())
		.andReturn();
	}
	
	//----- Stock services tests
	@Test
	public void createStockTestWithInvalidToken() throws Exception {
		mockMvc.perform(post("/api/v1/stock")
				.header("token", "nothing")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{}")
			.characterEncoding("utf-8"))
		.andDo(print())
		.andExpect(status().isUnauthorized())
		.andReturn();
	}
	
	@Test
	public void createStockTest() throws Exception {
		JSONObject newStockJson = new JSONObject();
		newStockJson.put("symbolName", "AMAZON");
		newStockJson.put("currentPrice", 100);
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/stock")
						.header("token", token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(newStockJson.toString())
						.characterEncoding("utf-8")
						)
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertTrue(resultJson.has("id"));
		assertTrue(resultJson.has("symbolName"));
		assertTrue(resultJson.has("currentPrice"));
		assertTrue(resultJson.has("lastUpdateTimeStamp"));
		assertEquals(newStockJson.getString("symbolName"), resultJson.getString("symbolName"));
		assertEquals(newStockJson.getLong("currentPrice"), resultJson.getLong("currentPrice"));
	}

	@Test
	public void createStockNonUniqueNameTest() throws Exception {
		JSONObject newStockJson = new JSONObject();
		newStockJson.put("symbolName", "NonUniqueCompany");
		newStockJson.put("currentPrice", 200);
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/stock")
						.header("token", token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(newStockJson.toString())
						.characterEncoding("utf-8")
						)
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		//insert the second one
		result = 
				mockMvc.perform(post("/api/v1/stock")
						.header("token", token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(newStockJson.toString())
						.characterEncoding("utf-8")
						)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();
		
		JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertTrue(resultJson.has("message"));
	}
	
	@Test
	public void getAllStocks() throws Exception {
		MvcResult result = 
				mockMvc.perform(get("/api/v1/stock"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		JSONArray resultJson = new JSONArray(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertNotEquals(0, resultJson.length());
	}
	
	@Test
	public void getStockById() throws Exception {
		long id = initialStocksJsonArray.getJSONObject(0).getLong("id");
		MvcResult result = 
		mockMvc.perform(get("/api/v1/stock/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertEquals(id, resultJson.getLong("id"));
	}
	
	@Test
	public void getStockByInvalidId() throws Exception {
		long id = -1;
		MvcResult result = 
		mockMvc.perform(get("/api/v1/stock/" + id))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();
		
		JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertTrue(resultJson.has("message"));
	}
	
	@Test
	public void updateStockPriceTest() throws Exception {
		long id = initialStocksJsonArray.getJSONObject(0).getLong("id");
		double priceBeforeUpdate =  initialStocksJsonArray.getJSONObject(0).getDouble("currentPrice");
		double amountToAdd = 10;
		MvcResult result = 
				mockMvc.perform(put("/api/v1/stock/" + id + "/price")
						.header("token", token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(priceBeforeUpdate + amountToAdd))
						.characterEncoding("utf-8")
						)
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(resultJson);
		assertTrue(resultJson.has("id"));
		assertTrue(resultJson.has("symbolName"));
		assertTrue(resultJson.has("currentPrice"));
		assertTrue(resultJson.has("lastUpdateTimeStamp"));
		assertEquals(Double.valueOf(priceBeforeUpdate + amountToAdd), Double.valueOf(resultJson.getDouble("currentPrice")));
	}

}

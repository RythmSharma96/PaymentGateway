package com.payment.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.payment.demo.controller.PaymentController;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentControllerTest {
	
	private MockMvc mockMvc; 
	
	@Mock
	private PaymentUtility paymentUtility;
	
	@InjectMocks
	private PaymentController paymentController;
	
	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(paymentController,paymentUtility).build();
	}
	
	
	@Test
	public void cardTypeTest() throws Exception { //It should be a bad request (status 400) when cardtype is not in the JSON
		String reqjson = "{\r\n"
				+ " \"currency\": \"CAD\",\r\n"
				+ " \"card\": {\r\n"
				+ " \"number\": \"4123123223111\",\r\n"
				+ " \"expirationMonth\": \"4\",\r\n"
				+ " \"expirationYear\": \"2025\",\r\n"
				+ " \"cvv\": \"555\"\r\n"
				+ " }\r\n"
				+ "}";
		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(reqjson)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void uidNotSentTest() throws Exception { //It should not pass as X-UID is not provided in headers; user is also unauthorized
		String reqjson = "{ \r\n"
				+ " \"amount\": \"100\", \r\n"
				+ " \"currency\": \"USD\", \r\n"
				+ " \"type\": \"creditcard\", \r\n"
				+ " \"card\": { \r\n"
				+ " \"number\": \"4111111111111111\", \r\n"
				+ " \"expirationMonth\": \"2\", \r\n"
				+ " \"expirationYear\": \"2020\", \r\n"
				+ " \"cvv\": \"111\" \r\n"
				+ " } \r\n"
				+ "} \r\n"
				+ "";
		 mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(reqjson))
		.andExpect(status().isForbidden()).andExpect(jsonPath("$.status", is("Unauthorized User")));
	}
	
	@Test
	public void cardDetailsNullTest() throws Exception { //It should not pass as card details are null
		String reqjson = "{ \r\n"
				+ " \"amount\": \"100\", \r\n"
				+ " \"type\": \"creditcard\",\r\n"
				+ "  \"currency\" : \"USD\"\r\n"
				+ "} ";																	 
		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(reqjson))
		.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void currencyNullTest() throws Exception { //currency left null; should give 400
		String reqjson = "{ \r\n"
				+ " \"amount\": \"100\",  \r\n"
				+ " \"type\": \"creditcard\",\r\n"
				+ " \"card\": { \r\n"
				+ " \"number\": \"4111111111111111\", \r\n"
				+ " \"expirationMonth\": \"2\", \r\n"
				+ " \"expirationYear\": \"2020\", \r\n"
				+ " \"cvv\": \"111\" \r\n"
				+ " } \r\n"
				+ "} \r\n"
				+ "";
		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(reqjson)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void getWithoutHeaders() throws Exception { //because both headers are missing on the get request; it should fail(forbidden status)
		mockMvc.perform(get("/transaction")).andExpect(status().isForbidden()).andExpect(jsonPath("$[0].status", is("Unauthorized User")));
	}
}

package com.main.olacarro;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.swing.text.AbstractDocument.Content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OlacarroApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ListingRepository listingRepository;
	

	@Before
	public void deleteAllBeforeTests() throws Exception {
		listingRepository.deleteAll();
	}

	@Test
	public void emptyStringForInitialSearch() throws Exception {
		mockMvc.perform(get("/search")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}
	
	@Test
	public void insertOneVehicleListings() throws Exception {
		
		String test = "[\n" + 
					"	{\n" + 
					"		\"code\": \"b\",\n" + 
					"		\"make\": \"audi\",\n" + 
					"		\"model\": \"a4\",\n" + 
					"		\"kW\": 132,\n" + 
					"		\"year\": 2014,\n" + 
					"		\"color\": \"red\",\n" + 
					"		\"price\": 13990\n" + 
					"	}\n" + 
					"]";
		
		mockMvc.perform(post("/vehicle_listings")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
            .andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"red\",\"price\":13990}]"));
	}
	
	
	@Test
	public void insertManyVehicleListings() throws Exception {

		String test = "[\n" + 
							"{" + 
								"\"code\": \"b\"," + 
								"\"make\": \"audi\"," + 
								"\"model\": \"a4\"," + 
								"\"kW\": 132," + 
								"\"year\": 2014," + 
								"\"color\": \"red\"," + 
								"\"price\": 13990" + 
							"}," + 
							"{" + 
								"\"code\": \"c\"," + 
								"\"make\": \"mercedes\"," + 
								"\"model\": \"a 180\"," + 
								"\"kW\": 132," + 
								"\"year\": 2016," + 
								"\"color\": \"black\"," + 
								"\"price\": 17970" + 
							"}" + 
						"]";
		
		mockMvc.perform(post("/vehicle_listings")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
		            .andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"red\",\"price\":13990},"
		            		+ "					{\"dealer\":\"\",\"code\":\"c\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"black\",\"price\":17970}]"));
	}

	public void updateManyVehicleListings() throws Exception {
		
		String test1 = "[\n" + 
				"{" + 
					"\"code\": \"b\"," + 
					"\"make\": \"audi\"," + 
					"\"model\": \"a4\"," + 
					"\"kW\": 132," + 
					"\"year\": 2014," + 
					"\"color\": \"red\"," + 
					"\"price\": 13990" + 
				"}," + 
				"{" + 
					"\"code\": \"c\"," + 
					"\"make\": \"mercedes\"," + 
					"\"model\": \"a 180\"," + 
					"\"kW\": 132," + 
					"\"year\": 2016," + 
					"\"color\": \"black\"," + 
					"\"price\": 17970" + 
				"}" + 
			"]";
	
		String test2 = "[\n" + 
					"{" + 
					"\"code\": \"b\"," + 
					"\"make\": \"audi\"," + 
					"\"model\": \"a4\"," + 
					"\"kW\": 132," + 
					"\"year\": 2014," + 
					"\"color\": \"black\"," + 
					"\"price\": 11990" + 
				"}" +
		"]";
		

		mockMvc.perform(post("/vehicle_listings")
				.accept(MediaType.APPLICATION_JSON).content(test1)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
		            .andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"red\",\"price\":13990},"
		            						 + "{\"dealer\":\"\",\"code\":\"c\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"black\",\"price\":17970}]"));

		
		mockMvc.perform(post("/vehicle_listings")
				.accept(MediaType.APPLICATION_JSON).content(test2)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
		            .andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"black\",\"price\":11990}]"));
		
		mockMvc.perform(get("/search?make=audi")).andDo(print())			
		.andExpect(status().isOk())
		.andExpect(content().json("{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"black\",\"price\":11990}]"));
		
	}

	public void testSearch() throws Exception {
	
	
	String test1 = "[\n" + 
			"{" + 
				"\"code\": \"b\"," + 
				"\"make\": \"audi\"," + 
				"\"model\": \"a4\"," + 
				"\"kW\": 132," + 
				"\"year\": 2015," + 
				"\"color\": \"red\"," + 
				"\"price\": 13990" + 
			"}," + 
			"{" + 
				"\"code\": \"c\"," + 
				"\"make\": \"mercedes\"," + 
				"\"model\": \"a 180\"," + 
				"\"kW\": 132," + 
				"\"year\": 2016," + 
				"\"color\": \"black\"," + 
					"\"price\": 17970" + 
			"}" +  
			"{" + 
				"\"code\": \"d\"," + 
				"\"make\": \"mercedes\"," + 
				"\"model\": \"a 180\"," + 
				"\"kW\": 132," + 
				"\"year\": 2015," + 
				"\"color\": \"black\"," + 
				"\"price\": 17970" + 
			"}" + 
	"]";


	mockMvc.perform(post("/vehicle_listings")
			.accept(MediaType.APPLICATION_JSON).content(test1)
			.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
	            .andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"red\",\"price\":13990},"
	            						 + "{\"dealer\":\"\",\"code\":\"c\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"black\",\"price\":17970}]"));
	
	mockMvc.perform(get("/search?make=audi")).andDo(print())			
	.andExpect(status().isOk())
	.andExpect(content().json("[{\"dealer\":\"\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"black\",\"price\":11990}]"));

}


	@Test
	public void searchRenault() throws Exception {
		mockMvc.perform(get("/search")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}
	
	

}

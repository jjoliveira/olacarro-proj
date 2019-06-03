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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

	@Test // 1) Search on an empty repository returns an empty JSON array; 
	public void emptyStringForInitialSearch() throws Exception {
		mockMvc.perform(get("/search")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}
	
	
	@Test // 2) Inserts one `Listing` in the database through `/vehicles_listing`
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
		
		mockMvc.perform(post("/vehicle_listings/Dealer1")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
            .andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":132,\"year\":2014,\"color\":\"red\",\"price\":13990}]"));
	}
	
	@Test // 3) Inserts many `Listing`s in the database through `/vehicles_listing` 
	public void insertManyVehicleListings() throws Exception {

		String test = "[\n" + 
							"{" + 
								"\"code\": \"a\"," + 
								"\"make\": \"audi\"," + 
								"\"model\": \"a4\"," + 
								"\"kW\": 250," + 
								"\"year\": 2014," + 
								"\"color\": \"red\"," + 
								"\"price\": 13990" + 
							"}," + 
							"{" + 
								"\"code\": \"b\"," + 
								"\"make\": \"mercedes\"," + 
								"\"model\": \"c 250\"," + 
								"\"kW\": 132," + 
								"\"year\": 1996," + 
								"\"color\": \"black\"," + 
								"\"price\": 17970" + 
							"}," + 
							"{" + 
								"\"code\": \"d\"," + 
								"\"make\": \"opel\"," + 
								"\"model\": \"corsa\"," + 
								"\"kW\": 54," + 
								"\"year\": 2016," + 
								"\"color\": \"black\"," + 
								"\"price\": 25970" + 
							"}," +
							"{" + 
								"\"code\": \"e\"," + 
								"\"make\": \"opel\"," + 
								"\"model\": \"corsa\"," + 
								"\"kW\": 74," + 
								"\"year\": 1996," + 
								"\"color\": \"black\"," + 
								"\"price\": 25970" + 
							"}," +
							"{" + 
								"\"code\": \"f\"," + 
								"\"make\": \"mercedes\"," + 
								"\"model\": \"a 180\"," + 
								"\"kW\": 132," + 
								"\"year\": 2016," + 
								"\"color\": \"blue\"," + 
								"\"price\": 17970" + 
							"}" +
						"]";
		
		mockMvc.perform(post("/vehicle_listings/Dealer2")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
		            .andExpect(content().json("[{\"dealer\":\"Dealer2\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer2\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer2\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
	}
	
	@Test // 4) Inserts many `Listing`s in the database through `/upload_csv`
	public void insertManyUploadCSV() throws Exception {
		/*
		 MockMultipartFile file = new MockMultipartFile("data", "test.csv", "text/plain", ("code,make/model,power-in-ps,year,color,price\n" + 
																					 	  "1,mercedes/a 180,123,2014,black,15950\n" + 
																					 	  "2,audi/a3,111,2016,white,17210\n" + 
																					 	  "3,vw/golf,86,2018,green,14980\n" + 
																					 	  "4,skoda/octavia,86,2018,16990").getBytes());
																				        
		 mockMvc.perform(MockMvcRequestBuilders.multipart("/upload_csv/Dealer4")
                 .file(file))
		 			.andExpect(status().isOk())
			        .andExpect(content().json("[{\"dealer\":\"Dealer4\",\"code\":\"1\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":123,\"year\":2014,\"color\":\"black\",\"price\":15950},{\"dealer\":\"Dealer4\",\"code\":\"2\",\"make\":\"audi\",\"model\":\"a3\",\"kW\":111,\"year\":2016,\"color\":\"white\",\"price\":17210},{\"dealer\":\"Dealer4\",\"code\":\"3\",\"make\":\"vw\",\"model\":\"golf\",\"kW\":86,\"year\":2018,\"color\":\"green\",\"price\":14980},{\"dealer\":\"Dealer4\",\"code\":\"4\",\"make\":\"skoda\",\"model\":\"octavia\",\"kW\":86,\"year\":2018,\"color\":null,\"price\":16990}]"));
		*/
	}
	
	@Test // 5) Search for `Listing`s with `make` mercedes and `model` c250;
	public void searchMercedesC250() throws Exception {
		String test = "[\n" + 
				"    {\n" + 
				"        \"code\": \"a\",\n" + 
				"        \"make\": \"audi\",\n" + 
				"        \"model\": \"a4\",\n" + 
				"        \"kW\": 250,\n" + 
				"        \"year\": 2014,\n" + 
				"        \"color\": \"red\",\n" + 
				"        \"price\": 13990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"b\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 17970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"d\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 54,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"e\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 74,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"f\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"grey\",\n" + 
				"        \"price\": 29990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"g\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"a 180\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"blue\",\n" + 
				"        \"price\": 17970\n" + 
				"    }\n" + 
				"]";

		mockMvc.perform(post("/vehicle_listings/Dealer3")
			.accept(MediaType.APPLICATION_JSON).content(test)
			.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		        .andExpect(content().json("[{\"dealer\":\"Dealer3\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer3\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer3\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer3\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer3\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer3\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
		mockMvc.perform(get("/search?make=mercedes&model=c 250")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[{\"dealer\":\"Dealer3\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer3\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990}]"));
	}
	
	@Test // 6) Search for `Listing`s with `color` black and `year` 1996;
	public void searchBlack1996() throws Exception {
		String test = "[\n" + 
				"    {\n" + 
				"        \"code\": \"a\",\n" + 
				"        \"make\": \"audi\",\n" + 
				"        \"model\": \"a4\",\n" + 
				"        \"kW\": 250,\n" + 
				"        \"year\": 2014,\n" + 
				"        \"color\": \"red\",\n" + 
				"        \"price\": 13990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"b\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 17970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"d\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 54,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"e\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 74,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"f\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"grey\",\n" + 
				"        \"price\": 29990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"g\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"a 180\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"blue\",\n" + 
				"        \"price\": 17970\n" + 
				"    }\n" + 
				"]";

		mockMvc.perform(post("/vehicle_listings/Dealer1")
			.accept(MediaType.APPLICATION_JSON).content(test)
			.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		        .andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer1\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer1\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
		
		mockMvc.perform(get("/search?color=black&year=1996")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer1\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970}]"));
		
		}
	
	@Test // 7) Search for all `Listing`s;
	public void searchAll() throws Exception {
		String test = "[\n" + 
				"    {\n" + 
				"        \"code\": \"a\",\n" + 
				"        \"make\": \"audi\",\n" + 
				"        \"model\": \"a4\",\n" + 
				"        \"kW\": 250,\n" + 
				"        \"year\": 2014,\n" + 
				"        \"color\": \"red\",\n" + 
				"        \"price\": 13990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"b\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 17970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"d\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 54,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"e\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 74,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"f\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"grey\",\n" + 
				"        \"price\": 29990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"g\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"a 180\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"blue\",\n" + 
				"        \"price\": 17970\n" + 
				"    }\n" + 
				"]";

		mockMvc.perform(post("/vehicle_listings/Dealer1")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
			        .andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer1\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer1\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
		
		mockMvc.perform(post("/vehicle_listings/Dealer2")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
			        .andExpect(content().json("[{\"dealer\":\"Dealer2\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer2\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer2\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer2\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
			        
		mockMvc.perform(get("/search")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer1\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer1\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970},{\"dealer\":\"Dealer2\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer2\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer2\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer2\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer2\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
		
	}
	
	@Test // 8) Inserts `Listing`s with existing code for a certain dealer through `/vehicles_listing`.
	public void insertExistingCode() throws Exception {
		String test = "[\n" + 
				"    {\n" + 
				"        \"code\": \"a\",\n" + 
				"        \"make\": \"audi\",\n" + 
				"        \"model\": \"a4\",\n" + 
				"        \"kW\": 250,\n" + 
				"        \"year\": 2014,\n" + 
				"        \"color\": \"red\",\n" + 
				"        \"price\": 13990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"b\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 17970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"d\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 54,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"e\",\n" + 
				"        \"make\": \"opel\",\n" + 
				"        \"model\": \"corsa\",\n" + 
				"        \"kW\": 74,\n" + 
				"        \"year\": 1996,\n" + 
				"        \"color\": \"black\",\n" + 
				"        \"price\": 25970\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"f\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"c 250\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"grey\",\n" + 
				"        \"price\": 29990\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"code\": \"g\",\n" + 
				"        \"make\": \"mercedes\",\n" + 
				"        \"model\": \"a 180\",\n" + 
				"        \"kW\": 132,\n" + 
				"        \"year\": 2016,\n" + 
				"        \"color\": \"blue\",\n" + 
				"        \"price\": 17970\n" + 
				"    }\n" + 
				"]";
		
		String test2 = "[\n" + 
				"    {\n" + 
				"        \"code\": \"a\",\n" + 
				"        \"make\": \"audi\",\n" + 
				"        \"model\": \"a4\",\n" + 
				"        \"kW\": 270,\n" + 
				"        \"year\": 2014,\n" + 
				"        \"color\": \"white\",\n" + 
				"        \"price\": 14500\n" + 
				"    }\n"+
				"]";
		
		mockMvc.perform(post("/vehicle_listings/Dealer1")
				.accept(MediaType.APPLICATION_JSON).content(test)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
			        .andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":250,\"year\":2014,\"color\":\"red\",\"price\":13990},{\"dealer\":\"Dealer1\",\"code\":\"b\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":1996,\"color\":\"black\",\"price\":17970},{\"dealer\":\"Dealer1\",\"code\":\"d\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":54,\"year\":2016,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"e\",\"make\":\"opel\",\"model\":\"corsa\",\"kW\":74,\"year\":1996,\"color\":\"black\",\"price\":25970},{\"dealer\":\"Dealer1\",\"code\":\"f\",\"make\":\"mercedes\",\"model\":\"c 250\",\"kW\":132,\"year\":2016,\"color\":\"grey\",\"price\":29990},{\"dealer\":\"Dealer1\",\"code\":\"g\",\"make\":\"mercedes\",\"model\":\"a 180\",\"kW\":132,\"year\":2016,\"color\":\"blue\",\"price\":17970}]"));
		
		mockMvc.perform(post("/vehicle_listings/Dealer1")
				.accept(MediaType.APPLICATION_JSON).content(test2)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
			        .andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":270,\"year\":2014,\"color\":\"white\",\"price\":14500}]"));
		
		mockMvc.perform(get("/search?make=audi&model=a4")).andDo(print())			
			.andExpect(status().isOk())
			.andExpect(content().json("[{\"dealer\":\"Dealer1\",\"code\":\"a\",\"make\":\"audi\",\"model\":\"a4\",\"kW\":270,\"year\":2014,\"color\":\"white\",\"price\":14500}]"));
	
	}
	
	

}

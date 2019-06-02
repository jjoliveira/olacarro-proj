package com.main.olacarro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ListingController {
	@Autowired
	ListingRepository listingRepository;
	
	@GetMapping("/search")
	public Iterable<Listing> search(@RequestParam(required=false) String make,
					   @RequestParam(required=false) String model,
					   @RequestParam(required=false) Integer year,
					   @RequestParam(required=false) String color) {
		
		
	}
	
	@PostMapping("/vehicle_listings")
    public Iterable<Listing> vehicleListings(@RequestBody List<Listing> list) {
		
    }
	
	@PostMapping("/upload_csv/{dealer_id}")
    public Iterable<Listing> uploadCSV(@PathVariable(required=true) String dealer_id, @RequestParam("file") MultipartFile csv) throws IOException {
		
    }


}

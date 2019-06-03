package com.main.olacarro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	MongoOperations mongoOps = (MongoOperations) new AnnotationConfigApplicationContext(AppConfig.class).getBean("mongoTemplate");
	
	@GetMapping("/search")
	public Iterable<Listing> search(@RequestParam(required=false) String make,
					   @RequestParam(required=false) String model,
					   @RequestParam(required=false) Integer year,
					   @RequestParam(required=false) String color) {
		Query query = new Query();
		if (make != null)
			query.addCriteria(Criteria.where("make").is(make));
		if (model != null)
			query.addCriteria(Criteria.where("model").is(model));
		if (year != null)
			query.addCriteria(Criteria.where("year").is(year));
		if (color != null)
			query.addCriteria(Criteria.where("color").is(color));
		
		return mongoOps.find(query, Listing.class);
		
	}
	
	@PostMapping("/vehicle_listings//{dealer_id}")
    public Iterable<Listing> vehicleListings(@PathVariable(required=true) String dealer_id, @RequestBody List<Listing> list) {
		for( int i = 0; i < list.size(); i++) {
			Listing temp = list.get(i);
			temp.setDealer(dealer_id);
			list.set(i, temp);
		}
		return listingRepository.saveAll(list);
    }
	
	@PostMapping("/upload_csv/{dealer_id}")
    public Iterable<Listing> uploadCSV(@PathVariable(required=true) String dealer_id, @RequestParam("file") MultipartFile csv) throws IOException {
		
		return listingRepository.saveAll(parseCSV(dealer_id, csv));
    }

	private List<Listing> parseCSV(String dealer_id, MultipartFile csv) throws IOException {
		
		List<Listing> retList = new ArrayList<Listing>();
		
		File tempFile = File.createTempFile("temp", ".csv");
		
		csv.transferTo(tempFile);
		
		BufferedReader br = new BufferedReader(new FileReader(tempFile));
        String line = br.readLine(); //reads header line
            
        while ((line = br.readLine()) != null) {
        	retList.add(parseLine(dealer_id, line));    
        }
        br.close();
		return retList;
	}

	private Listing parseLine(String dealer_id, String line) {
		String[] aux = line.split(",");
		
		String code = aux[0];
		String make = aux[1].split("/")[0];
		String model = aux[1].split("/")[1]; 
		int kW = (int) (Integer.parseInt(aux[2]) * 0.73549875); // according to https://www.unitconverters.net/power/pferdestarke-ps-to-kilowatt.htm
		int year = Integer.parseInt(aux[3]); 
		String color = aux.length == 6 ? aux[4] : null; //assuming sometimes color can be missing like in the provided example
		int price = aux.length == 6 ? Integer.parseInt(aux[5]) : Integer.parseInt(aux[4]);
		Listing retListing = new Listing(code, make, model, kW, year, color, price);
		
		retListing.setDealer(dealer_id);
		
		return retListing;
	}

}

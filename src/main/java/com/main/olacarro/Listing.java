package com.main.olacarro;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 
 * @author jjoliveira
 *
 * Class containing the data of each listing
 *
 */

@CompoundIndex(name = "dealer_code", def="{'dealer': 1, 'code': 1 }")
@Document
public class Listing {
	
	@Field("dealer") private String dealer;
	@Field("code") private String code;
	private String make;
	private String model;
	private Integer kW;
	private Integer year;
	private String color;
    private Integer price;
    
    public Listing(String code, String make, String model, int kW,  int year,  String color, int price) {
        super();
        this.dealer = "";
    	this.code = code;
		this.make = make;
		this.model = model;
		this.kW = kW;
		this.year = year;
		this.color = color;
        this.price = price;
    }

	/**
	 * @return the dealer
	 */
	public String getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the kW
	 */
	public Integer getkW() {
		return kW;
	}

	/**
	 * @param kW the kW to set
	 */
	public void setkW(Integer kW) {
		this.kW = kW;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
    
}

package com.cogito.oms.data.model;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Entity
public class Location extends AbstractEntity {

	public Location(String name, double longitude, double latitude) {
		super();
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Location(){
	}
	
	private String name;
	private double longitude;
	private double latitude;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
}

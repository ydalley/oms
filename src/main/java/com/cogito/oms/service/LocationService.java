package com.cogito.oms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Location;

public interface LocationService {

	public String addLocation(Location location) throws ApplicationException;

	public String updateLocation(Location location) throws ApplicationException;

	public String deleteLocation(Location location) throws ApplicationException;

	public Location getLocation(Long Id);

	public List<Location> getAllLocations();

	public Page<Location> getLocations(Pageable page);

}

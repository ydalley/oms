package com.cogito.oms.service.implementation;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.Location;
import com.cogito.oms.data.repository.LocationRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.LocationService;
import com.cogito.oms.util.Messages;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepo;
	@Autowired
	Messages messages;
	@Autowired
	public LocationServiceImpl(LocationRepository locationRepo) {
		this.locationRepo = locationRepo;
	}

	@Override
	public String addLocation(Location location) throws ApplicationException {
		locationRepo.save(location);
		return messages.get("location.saved");
	}

	@Override
	public String updateLocation(Location location) throws ApplicationException {
		locationRepo.save(location);
		return messages.get("location.updated");
	}

	@Override
	public String deleteLocation(Location location) throws ApplicationException {
		locationRepo.delete(location);
		return messages.get("location.deleted");
	}

	@Override
	public Location getLocation(Long id) {
		return locationRepo.getOne(id);
	}

	@Override
	public List<Location> getAllLocations() {
		return locationRepo.findAll();
	}

	@Override
	public Page<Location> getLocations(Pageable page) {
		return locationRepo.findAll(page);
	}

}

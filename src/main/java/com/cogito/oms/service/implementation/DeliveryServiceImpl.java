package com.cogito.oms.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Delivery;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.DeliveryService;

public class DeliveryServiceImpl implements DeliveryService {

	@Override
	public String makeDelivery(Delivery delivery) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateDelivery(Delivery delivery) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelDelivery(Delivery delivery) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Delivery getDelivery(Long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Delivery> getAllDeliveries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Delivery> getDeliveries(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

}

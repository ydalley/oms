package com.cogito.oms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Delivery;
import com.cogito.oms.data.model.Order;

public interface DeliveryService {

	public String makeDelivery(Delivery delivery) throws ApplicationException;

	public String updateDelivery(Delivery delivery) throws ApplicationException;

	public String cancelDelivery(Delivery delivery) throws ApplicationException;

	public Delivery getDelivery(Long Id);

	public List<Delivery> getAllDeliveries();

	public Page<Delivery> getDeliveries(Pageable page);

}

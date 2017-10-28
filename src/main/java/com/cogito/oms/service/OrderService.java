package com.cogito.oms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Agent;
import com.cogito.oms.data.model.Order;

public interface OrderService {

	public String addOrder(Order order) throws ApplicationException;

	public String modifyOrder(Order order) throws ApplicationException;

	public String cancelOrder(Order order) throws ApplicationException;
	
	public String checkOutForDelivery(Order order, Agent driver) throws ApplicationException;

	public Order getOrder(Long Id);

	public List<Order> getAllOrders();

	public Page<Order> getOrders(Pageable page);

}

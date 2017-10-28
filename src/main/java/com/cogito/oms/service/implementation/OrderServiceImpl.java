package com.cogito.oms.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.Agent;
import com.cogito.oms.data.model.Order;
import com.cogito.oms.data.model.OrderStatus;
import com.cogito.oms.data.repository.OrderRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepo;
	
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@Override
	public String addOrder(Order order) throws ApplicationException {
		order.setStatus(OrderStatus.PROCESSING);
		orderRepo.save(order);
		return "added.order";
	}

	@Override
	public String modifyOrder(Order order) throws ApplicationException {
		orderRepo.save(order);
		return "modified.order";
	}

	@Override
	public String cancelOrder(Order order) throws ApplicationException {
		order.setStatus(OrderStatus.CANCELLED);
		orderRepo.save(order);
		return "cancelled.order";
	}

	@Override
	public String checkOutForDelivery(Order order, Agent driver) throws ApplicationException {
		// TODO Auto-generated method stub
		//order.set
		order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
		orderRepo.save(order);
		return "checkout.order";
	}

	@Override
	public Order getOrder(Long id) {
		return orderRepo.getOne(id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public Page<Order> getOrders(Pageable page) {
		return orderRepo.findAll(page);
	}

}

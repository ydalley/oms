package com.cogito.oms.data.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="[order]")
public class Order extends AbstractEntity{

	private String comments;
	private Date orderDate;
	private Date deliveryDate;
	private OrderStatus status;
	@ManyToOne
	private Location shippingLocation;
	@ManyToOne
	private Location deliveryLocation;
	
	@ManyToOne
	private Customer customer;
	
	@OneToMany
	private List<OrderEntry> items;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderEntry> getItems() {
		return items;
	}

	public void setItems(List<OrderEntry> items) {
		this.items = items;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Location getShippingLocation() {
		return shippingLocation;
	}

	public void setShippingLocation(Location shippingLocation) {
		this.shippingLocation = shippingLocation;
	}

	public Location getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(Location deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	
	
}

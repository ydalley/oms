package com.cogito.oms.data.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OrderEntry extends AbstractEntity {
	
	@ManyToOne
	private Goods item;
	private int quantity;
	private String comments;
	public Goods getItem() {
		return item;
	}
	public void setItem(Goods item) {
		this.item = item;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}

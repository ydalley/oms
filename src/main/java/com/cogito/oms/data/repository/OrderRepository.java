package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Order;


@Repository
public interface OrderRepository extends CommonRepository<Order,Long> {
	
}

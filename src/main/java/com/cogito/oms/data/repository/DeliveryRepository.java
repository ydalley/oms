package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Delivery;


@Repository
public interface DeliveryRepository extends CommonRepository<Delivery,Long> {
	
}

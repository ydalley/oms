package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Agent;
import com.cogito.oms.data.model.Customer;


@Repository
public interface CustomerRepository extends CommonRepository<Customer,Long> {
	
}

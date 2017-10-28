package com.cogito.oms.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Customer;

public interface CustomerService {
	Customer getCustomer(long id);
	Page<Customer> getAllCustomers(Pageable pageDetails);
	Page<Customer> findCustomer(String pattern,Pageable pageDetails);
	String modify(Customer customer) throws ApplicationException;
	String add(Customer customer) throws ApplicationException;
}

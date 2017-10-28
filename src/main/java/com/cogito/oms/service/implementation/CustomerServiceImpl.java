package com.cogito.oms.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.Customer;
import com.cogito.oms.data.repository.CustomerRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.CustomerService;
import com.cogito.oms.util.Messages;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	Messages messages;
	
	@Override
	public Customer getCustomer(long id) {
		return customerRepo.getOne(id);
	}

	@Override
	public Page<Customer> getAllCustomers(Pageable pageDetails) {
		return customerRepo.findAll(pageDetails);
	}

	@Override
	public Page<Customer> findCustomer(String pattern, Pageable pageDetails) {
		return customerRepo.findUsingPattern(pattern, pageDetails);
	}

	@Override
	public String modify(Customer customer) throws ApplicationException {
		customerRepo.save(customer);
		return messages.get("customer.modified");
	}

	@Override
	public String add(Customer customer) throws ApplicationException {
		customerRepo.save(customer);
		return messages.get("customer.added");
	}

}

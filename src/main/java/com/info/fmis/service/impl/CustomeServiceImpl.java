package com.info.fmis.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.model.Customer;
import com.info.fmis.repository.CustomeRepository;
import com.info.fmis.service.CustomerService;

@Service
public class CustomeServiceImpl implements CustomerService {

	@Autowired
	CustomeRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Customer save(CustomerDTO customerDTO) {

		Customer customer = this.modelMapper.map(customerDTO, Customer.class);
		customer = customerRepository.save(customer);
		return customer;
	}
}
package com.info.fmis.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.model.Customer;

public interface CustomerService  {

	Customer save(CustomerDTO customerDTO);

	Page<Customer> findAll(Pageable pageable);

	Customer update(CustomerDTO customerUpdate);
}
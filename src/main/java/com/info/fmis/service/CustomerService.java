package com.info.fmis.service;

import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.model.Customer;

public interface CustomerService  {

	Customer save(CustomerDTO customerDTO);
}
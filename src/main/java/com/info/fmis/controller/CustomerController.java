package com.info.fmis.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.model.Customer;
import com.info.fmis.repository.CustomeRepository;
import com.info.fmis.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

	private Logger LOGGER = Logger.getLogger(CustomerController.class);

	@Autowired
	CustomeRepository customerRepository;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/customers")
	public ResponseEntity<List<CustomerDTO>>  getCustomers(Pageable pageable) {
		
		Page<Customer> page = customerService.findAll(pageable);
		List<CustomerDTO> result = page.stream().map(obj -> modelMapper.map(obj, CustomerDTO.class))
				.collect(Collectors.toList());
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
		return ResponseEntity.ok().headers(headers).body(result);
	}
	
	@PostMapping("/customers")
	public ResponseEntity<CustomerDTO>  create(@Valid @RequestBody CustomerDTO customerDTO) {
		Customer obj =  customerService.save(customerDTO);
		CustomerDTO result = this.modelMapper.map(obj, CustomerDTO.class);
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/customers/{id}")
	public ResponseEntity<CustomerDTO> update(@Valid @RequestBody CustomerDTO customerUpdate) {
		Customer obj = customerService.update(customerUpdate);
		CustomerDTO result = this.modelMapper.map(obj, CustomerDTO.class);
		return ResponseEntity.ok().body(result);
	}
	
	@DeleteMapping("/customers/{id}")
	public void delete(@PathVariable(value = "id") Long customerId) {
		 customerRepository.deleteById(customerId);
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> findById(@PathVariable(value = "id") Long customerId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		
		if(customer == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(customer);
	}

}

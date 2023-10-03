package com.info.fmis.controller;

import java.util.List;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.model.Customer;
import com.info.fmis.repository.CustomeRepository;
import com.info.fmis.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private Logger LOGGER = Logger.getLogger(CustomerController.class);

	@Autowired
	CustomeRepository customerRepository;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String test() {
		LOGGER.info("get all customers");
		return "REST API test success!";
	}
	
	@GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
	public List<Customer> get() {
		LOGGER.info("get all customers");
		return customerRepository.findAll();
	}

	@GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public String hello() {
		return "hello world api called";
	}
	
	@PostMapping("/create")
	public ResponseEntity<CustomerDTO>  create(@Valid @RequestBody CustomerDTO customerDTO) {
		Customer obj =  customerService.save(customerDTO);
		CustomerDTO result = this.modelMapper.map(obj, CustomerDTO.class);
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/update")
	public Customer update(@Valid @RequestBody Customer customerUpdate) {
		Customer customerFind = customerRepository.getReferenceById(customerUpdate.getId());
		customerFind.setFirstName(customerUpdate.getFirstName());
		customerFind.setLastName(customerUpdate.getLastName());
		return customerRepository.save(customerFind);
	}
	
	@PostMapping("/delete/{id}")
	public void delete(@PathVariable(value = "id") Long customerId) {
		 customerRepository.deleteById(customerId);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Customer> findById(@PathVariable(value = "id") Long customerId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		
		if(customer == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(customer);
	}

}

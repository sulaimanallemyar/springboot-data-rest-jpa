package com.info.fmis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.info.fmis.model.Customer;

@Repository
public interface CustomeRepository extends JpaRepository<Customer, Long> {

	 Customer findByUsername(String username);
}
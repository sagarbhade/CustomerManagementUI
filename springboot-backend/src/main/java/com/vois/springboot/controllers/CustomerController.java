package com.vois.springboot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vois.springboot.exception.ResourceNotFoundException;
import com.vois.springboot.models.Customer;
import com.vois.springboot.repository.CustomerRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class CustomerController {
	@Autowired
	private CustomerRepository customerRepository;
	
	//Get All Customers
	@GetMapping("/customers")
	public List<Customer> getAllCustomer(){
		return customerRepository.findAll();
	}
	
	// create customer rest api
	@PostMapping("/customers")
	@PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
	public Customer createCustomer(@RequestBody Customer customer) {
		return customerRepository.save(customer);
	}
	
	//get customer by id
	@GetMapping("customers/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Customer cust= customerRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Customer Not Found with Id :"+ id));
	return ResponseEntity.ok(cust);
	}
	
	//update Customer
	@PutMapping("customers/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerNew){
		Customer custExisting= customerRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Customer Not Found with Id :"+ id));
		custExisting.setFirstName(customerNew.getFirstName());
		custExisting.setLastName(customerNew.getLastName());
		custExisting.setMobile(customerNew.getMobile());
		custExisting.setEmailId(customerNew.getEmailId());
		Customer updatedCustomer=customerRepository.save(custExisting);
		return ResponseEntity.ok(updatedCustomer);
	}
	
	//Delete Customer
	@DeleteMapping("/customers/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Boolean>> deleteCustomerById(@PathVariable Long id){
		Customer customer=customerRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Customer not found with Id :"+ id));
		customerRepository.delete(customer);
		Map<String, Boolean> response= new HashMap<String, Boolean>();
		response.put("Customer deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}

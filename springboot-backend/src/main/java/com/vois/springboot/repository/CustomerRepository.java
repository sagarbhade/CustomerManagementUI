package com.vois.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vois.springboot.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}

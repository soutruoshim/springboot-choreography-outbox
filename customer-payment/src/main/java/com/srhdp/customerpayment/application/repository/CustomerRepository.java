package com.srhdp.customerpayment.application.repository;

import com.srhdp.customerpayment.application.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

}

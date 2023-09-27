package com.springdemo.dao;

import java.util.List;

import com.springdemo.entity.Customer;

public interface CustomerDAO {
	
	public List<Customer> getCustomers();

	public void saveCustomer(Customer theCustomer);

	public Customer getCustomerById(int theId);

	public void deleteCustomer(int theId);

	public List<Customer> searchCustomer(String theSearchName);
}

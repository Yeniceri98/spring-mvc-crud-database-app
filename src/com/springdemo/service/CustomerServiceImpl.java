package com.springdemo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springdemo.dao.CustomerDAO;
import com.springdemo.entity.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	// Need to inject Customer DAO
	@Autowired
	private CustomerDAO customerDAO;
	
	@Override
	@Transactional		// Service class'ını oluşturduktan sonra CustomerDAOImpl'dan buraya taşıdık
	public List<Customer> getCustomers() {
		return customerDAO.getCustomers();				// NOT: customer'ı customerDAO çeker (Hibernate ile ilşkili olan o)
	}

	@Override
	@Transactional
	public void saveCustomer(Customer theCustomer) {		
		customerDAO.saveCustomer(theCustomer);			// NOT: customerDAO'dan save eder. O yüzden CustomerDAOImpl'da save kısmını yaptık
	}

	@Override
	@Transactional
	public Customer getCustomerById(int theId) {
		return customerDAO.getCustomerById(theId);
	}

	@Override
	@Transactional
	public void deleteCustomer(int theId) {
		customerDAO.deleteCustomer(theId);
		
	}

	@Override
	@Transactional
	public List<Customer> searchCustomer(String theSearchName) {
		return customerDAO.searchCustomer(theSearchName);
	}

	

}

/*
	@Service Annotation
	It's applied to Service implementations
	It annotates classes at the service layer
	Spring will automatically register the Service implementation thanks to component-scanning
	NOT: Service layer'ını oluşturma sebebimiz DAO layer'ından önce ara bir katman oluşturmak. CustomerDAO, SalesDAO, ClientDAO gibi birbirinden farklı DAO'lar olabilirdi
	
	Controller ---> Service ---> DAO (Project Overview.PNG de görülebilir)
*/
/*
	Question
	Why do we have to create Service layer what has the same functions as DAO layer? Is it necessary to create all this layers?

	Answer
	Agreed, there are a lot of layers. However this is the architecture that you will see on real world, complex Spring projects
	In our example, it is fairly simple. We simply delegate the calls to the DAO. So I agree, you could remove the service layer in this simple example and have controller call dao directly
	However, we added the service layer to leverage the Service Layer design pattern. On a much more complex project, we could use the service layer to integrate multiple data sources (daos) and perform transaction management between the two. So, for a simple project that we have here ... this probably overkill. However, I wanted to show you design patterns that you will encounter on real projects
*/
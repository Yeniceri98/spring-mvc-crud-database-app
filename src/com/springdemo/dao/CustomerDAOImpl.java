package com.springdemo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	// Need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;		// NOT: spring-mvc-crud-demo-servlet.xml'in 45. sayfasındaki beanId
	
	@Override
	// @Transactional		// NOT: Service class'ını oluşturunca buradan silip orada tanımladık. CustomerServiceImpl kısmında transaction'ı açıp kapatmak best practice olur
	public List<Customer> getCustomers() {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Create a query
		Query<Customer> theQuery = currentSession.createQuery("from Customer ORDER BY lastName", Customer.class);
		
		// Execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		// Return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Save the customer
		// currentSession.save(theCustomer);
		currentSession.saveOrUpdate(theCustomer);		// NOT: Üst satırdaki gibi "save()" veya "update()" metodunu da kullanabilirdik fakat bu şekildeyken customer yoksa save; varsa update eder
	}

	@Override
	public Customer getCustomerById(int theId) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Retrive customer from the database
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Delete object with the id
		
		// 1st Way of Deleting: With Session Directly
		currentSession.delete(theId);
		
		// 2nd Way of Deleting: With Query
		// Query theQuery = currentSession.createQuery("DELETE FROM Customer WHERE id=:customerId");
		// theQuery.setParameter("customerId", theId);
		// theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomer(String theSearchName) {
		
		// Get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = null;
		
		if (theSearchName != null && theSearchName.trim().length() > 0) {
			
			// Search for firstName or lastName ... case insensitive
            theQuery =currentSession.createQuery("FROM Customer WHERE lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
            
		} else {
			
			// theSearchName is empty ... so just get all customers
            theQuery =currentSession.createQuery("from Customer", Customer.class); 
		}
		
		// Execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		return customers;
	}
}

/*
	Data Access Object (DAO)	
	https://www.baeldung.com/java-dao-pattern#:~:text=The%20Data%20Access%20Object%20(DAO,mechanism)%20using%20an%20abstract%20API
	https://hasscript.com/755/yaz%C4%B1lmda-dao-nedir
	
	@Transactional Annotation
	Spring provides @Transactional annotation
	It automatically "begin" and "end" a transaction for Hibernate code
	There is no need for us to explicitly do this in the code. This happens behind the scene
	NOT: Start ve commit transaction yapıyorduk. Annotation sayesinde bu ikisine artık ihtiyaç olmaz
	
	@Repository Annotation
	DAO implementasyonlarında kullanılır
	Spring will automatically register the DAO implementation thanks to component-scanning
	Spring also provides translation of any JDBC related exceptions

	Controller ---> Service ---> DAO (Project Overview.PNG de görülebilir)
*/
/*
 	Customer DAO  <--->	 Session Factory  <--->  Data Source  <--->  Database
 	
 	For Hibernate
 	- DAO needs a Hibernate Session Factory
 	- Hibernate Session Factory neeeds a Data Source
 	- Data Source defines Database connection info
 	
 	These are all dependencies. We wire them together with Dependency Injection (DI)
*/
/*
	saveOrUpdate(...)
	if primary key or id is empty;
	then INSERT new customer
	else UPDATE existing customer
*/
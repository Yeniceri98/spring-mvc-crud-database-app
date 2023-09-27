package com.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springdemo.dao.CustomerDAO;
import com.springdemo.entity.Customer;
import com.springdemo.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	/*
	 	NOT: Service class'ını oluşturduktan sonra dependency injection'ını CustomerDAO'ya göre değil CustomerService'e göre yapmalıyız. O yüzden alt kısmı yoruma aldık
	 	NOT: listCustomers() metodunda da getCustomers'ı çekerken DAO üzerinden değil (yoruma aldım) Service üzerinden yaptık
	 	
	 	Controller ---> Service ---> DAO (Project Overview.PNG de görülebilir)
	 	
		// Need to inject the CustomerDao
		@Autowired
		private CustomerDAO customerDAO;
	*/
	
	// Need to inject the CustomerService
	@Autowired
	private CustomerService customerService;
	
	// @RequestMapping("/list")   // URL ---> http://localhost:8080/Database_Web_App_-_Spring_and_Hibernate/customer/list
	@GetMapping("/list")		  // NOT: RequestMapping tüm HTTP operasyonlarında çalışır. Eğer sadece spesifik bir operasyonda çalışmak istiyorsak @GetMapping @PostMapping gibi  annotationları kullanılabilir
	public String listCustomers(Model theModel) {		
		
		/*
			// Get customers from the DAO
			List<Customer> theCustomers = customerDAO.getCustomers();
		*/
		
		// Get customers from the Service
		List<Customer> theCustomers = customerService.getCustomers();
		
		// Add the customers to the Model
		theModel.addAttribute("customers", theCustomers);		// Name, Value
		
		return "list-customers";		// list-customers.jsp'yi çalıştırır
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		// Create model attribute to bind form data
		Customer theCustomer = new Customer();
		
		theModel.addAttribute("customer", theCustomer);		// "customer", customer-form.jsp'deki <form:form> taginde modelAttribute olarak bind edilir
		
		return "customer-form";
	}
	
	@PostMapping("/saveCustomer")		// customer-form.jsp'de <form:form action="saveCustomer" tanımlaması yapıldı
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		// Save the customer using the Service
		customerService.saveCustomer(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId, Model theModel) {
		
		// Get the customer from the Service
		Customer theCustomer = customerService.getCustomerById(theId);
		
		// Set the customer as a model attribute to pre-populate the form (Update Linke tıklayınca ilgili customer'ın bilgilerini getirecek ve sonrasında update edebileceğiz)
		// NOT: saveCustomer kısmında da "customer" bind edilmişti fakat onda arka planda customer.setFirstName() çalıştırılırken burada customer.getFirstName() çalıştırılmış olur
		theModel.addAttribute("customer", theCustomer);
		
		// Send over to our from
		return "customer-form";
	}
	
	@GetMapping("/delete")		// NOT: Delete işlemi için neden @GetMapping kullandığımızı aşağıdaki QUESTION-ANSWER kısmında açıkladım
	public String deleteCustomer(@RequestParam("customerId") int theId) {
		
		// Delete the customer
		customerService.deleteCustomer(theId);
		
		return "redirect:/customer/list";		// After completing deleting of specific student we need to reload all students again
	}
	
	@GetMapping("/search")
	public String searchCustomer(@RequestParam("theSearchName") String theSearchName, Model theModel) {
		
		// Search customers from the Service
		List<Customer> theCustomers = customerService.searchCustomer(theSearchName);
		
		// Add customers to the Model
		theModel.addAttribute("customers", theCustomers);
		
		return "list-customers";
	}
}

/*
 	QUESTION:
	Why do we have to create Service layer what has the same functions as DAO layer? Is it necessary to create all this layers?

	ANSWER:
	Agreed, there are a lot of layers. However this is the architecture that you will see on real world, complex Spring projects
	In our example, it is fairly simple. We simply delegate the calls to the DAO. So I agree, you could remove the service layer in this simple example and have controller call dao directly
	However, we added the service layer to leverage the Service Layer design pattern. On a much more complex project, we could use the service layer to integrate multiple data sources (daos) and perform transaction management between the two. So, for a simple project that we have here ... this probably overkill. However, I wanted to show you design patterns that you will encounter on real projects
*/
/*
 	QUESTION:
 	What is the actual and the briefest reason for using @ModelAttribute for saveCustomer controller and @RequestParam for showFormUpdate controller?
 	
 	ANSWER:
 	If I only have a small number of params 1-3 then I normally use @RequestParam
	If I have more params than 3 then I'll model the params as a class and use @ModelAttribute
	The count of 3 is subjective, but that is just my rule of thumb
	You can use the same guidance as when you create normal method parameters. Do you want to pass in many individual params or just pass in a single object?
	Finally, the benefits of using @ModelAttribute include form data binding and validation
	
	@ModelAttribute: Binds an entire Java object (like Employee). supports multiple request parameters
	@RequestParam: Binds a single request parameter (like firstName)

	In general,
	@RequestParam is best for reading a small number of params.
	@ModelAttribute is used when you have a form with a large number of fields.
	@ModelAttribute gives you additional features such as data binding, validation and form prepopulation
	
	Bizim örneğimize göre;
	saveCustomer kısmında tüm fieldları bind ederken @ModelAttribute kullandık
	showFormForUpdate kısmında ise sadece id üzerinden bind işlemi olduğunu için @RequestParam kullandık
*/ 
/*
 	QUESTION:
 	Why are using GET method for update and delete? Why didn't we use @DeleteMapping while deleting the customer?
 	
 	ANSWER:
 	When you are using Spring MVC, the web forms only support GET and POST method
 	Hence in this example, we made use of a @GetMapping to handle the delete
 	GET and POST is enough for building Spring MVC Web apps
*/
/*
 	QUESTION:
 	When should we use HTTP-DELETE instead of GET/POST?
 	
 	ANSWER:
 	You will use HTTP-DELETE when building REST APIs (Spring REST)
 	You may wonder, why not use HTTP-DELETE for Spring MVC web apps?
 	When using the default behavior of HTML forms, the HTML forms only support GET and POST. That is the reason we are not using HTTP-DELETE for our Spring MVC web app for CRUD
 	However, you can use @DeleteMapping but it is a more manual process and requires more coding. You will need to add support for JavaScript+jQuery+ajax
*/ 



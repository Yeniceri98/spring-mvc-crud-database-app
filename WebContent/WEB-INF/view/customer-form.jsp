<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html>

<head>
	<title>Save Customer</title>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/add-customer-style.css">
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>CRM - Customer RelationShip Manager</h2>
		</div>
		
		<div id="container">
			<h3>Save Customer</h3>
			
			<form:form action="saveCustomer" modelAttribute="customer" method="POST">
		
				<!-- 
					___ WHY WE USE FORM HIDDEN PATH FOR ID? ___
					Need to associate this data with customer id
					When the update form is submitted, the form data is bound to the "customer" model attribute. This binding is for all of the form fields: first name, last name, email AND id
					This data is submitted to the controller. Once the controller receives the customer object, it has the customer id already populated
					The data is then passed from controller -> service -> dao. In the dao code, we call the hibernate method "saveOrUpdate", Hibernate uses the id behind the scenes to perform the update
					As an experiment, you can remove the hidden form field for the id. as a result, the id will always be 0 by default. so when you perform an update, hibernate will see the id of 0 and that means that this a new object and Hibernate will insert a new record. it will not update the existing record. you will start to see duplicate entries because of the missing id
				-->
				<form:hidden path="id" />
			
				<table>
					<tbody>
						<tr>
							<td><label>First Name:</label></td>
							<td><form:input path="firstName" /></td>
						</tr>
						<tr>
							<td><label>Last Name:</label></td>
							<td><form:input path="lastName" /></td>
						</tr>
						<tr>
							<td><label>Email:</label></td>
							<td><form:input path="email" /></td>
						</tr>
						<tr>
							<td><input type="submit" value="Save" class="save" />
						</tr>
					</tbody>
				</table>
			</form:form>
			
			<div style="clear; both;">
				<p>
					<a href="${pageContext.request.contextPath}/customer/list">
						Customer List Page
					</a>
				</p>
			</div>
			
		</div>
	</div>
</body>

</html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SDProject</title>
</head>
<body>

	<c:if test="${session.username!= null}">
		<h3><font color="black" id="username"><c:out  value="Username: ${ session.username}"/></font></h3>
	</c:if>
	<hr>
	
	<s:text name="Prime numbers needed?" />
	<s:form action="primes" method="post">
		<s:textfield name="primesBean.number" />
		<s:submit />
	</s:form>
	
	<s:form action="availableProjects" method="get">
		<input type="submit" value="Show Available Projects">
	</s:form>
	<s:form action="olderProjects" method="get">
		<input type="submit" value="Show Older Projects">
	</s:form>
	<s:form action="projDetails" method="get">
		<input type="submit" value="Show Project Details">
	</s:form>
	<s:form action="register" method="post">
		<input type="submit" value="Register">
	</s:form>
	<s:form action="login" method="post">
		<input type="submit" value="Login">
	</s:form>  
	
	<s:form action="logout" method="post">
		<input type="submit" value="logout" >
	</s:form>  
	
	

	<input type="button" value="Exit" onclick="self.close()">
	     
</body>
</html>
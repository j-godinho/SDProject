<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SDProject</title>
</head>
<body>
	<s:text name="Prime numbers needed?" />
	<s:form action="primes" method="post">
		<s:textfield name="primesBean.number" />
		<s:submit />
	</s:form>
	
	<s:form action="availableProjects" method="post">
		<input type="submit" value="Show Available Projects">
	</s:form>
	<s:form action="olderProjects" method="post">
		<input type="submit" value="Show Older Projects">
	</s:form>
	<s:form action="projDetails" method="post">
		<input type="submit" value="Show Project Details">
	</s:form>
	<s:form action="register" method="post">
		<input type="submit" value="Register">
	</s:form>
	<s:form action="login" method="post">
		<input type="submit" value="Login">
	</s:form>  

	<input type="button" value="Exit" onclick="self.close()">
	     
</body>
</html>
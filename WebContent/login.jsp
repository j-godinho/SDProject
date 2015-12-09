<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css">
<title>Login</title>

</head>
<body>

	<!-- Top part -->
	<p><a href="<s:url action="index" />">Back</a></p>
	<hr>
	
	<c:if test="${session.AuthenticationError != null}">
		<h3><font color="red"><c:out value="${ session.AuthenticationError }"/></font></h3>
		<c:remove var="AuthenticationError" scope="session" />
	</c:if>
	<hr>
	
	<!-- Top part -->
	
	
	<s:text name="Login" />
	    <s:form action="loginUser" method="post">
	    <s:textfield name="AuthenticationBean.username" />
	    <s:password name="AuthenticationBean.password" />
	    <s:submit value="Login"/>
		</s:form>    
	
</body>
</html>
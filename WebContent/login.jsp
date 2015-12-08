<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>

</head>
<body>

	<c:if test="${session.AuthenticationError != null}">
		<h3><font color="blue"><c:out value="${ session.AuthenticationError }"/></font></h3>
		<c:remove var="AuthenticationError" scope="session" />
	</c:if>
	<hr>
	
	<s:text name="Login" />
	    <s:form action="authentication" method="post">
	    <s:textfield name="AuthenticationBean.username" />
	    <s:password name="AuthenticationBean.password" />
	    <s:submit value="Login"/>
		</s:form>    
	
</body>
</html>
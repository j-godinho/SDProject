<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
	
		<s:text name="Register" />
	    <s:form action="registration" method="post">
	    <s:textfield name="registrationBean.username" />
	    <s:password name="registrationBean.password1" />
	    <s:password name="registrationBean.password2" />
	    <s:submit value="Register"/>
		</s:form>    
</body>
</html>
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

	<s:text name="Login" />
	    <s:form action="authentication" method="post">
	    <s:textfield name="AuthenticationBean.username" />
	    <s:textfield name="AuthenticationBean.password" />
	    <s:submit value="Login"/>
		</s:form>    
	
</body>
</html>
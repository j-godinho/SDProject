<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Available Projects</title>
</head>
<body>
	<s:text name="Available projects" />
	<p>
	<c:forEach items="${availableProjects.projects}" var="value">
		<c:out value="${value}" /><br>
	</c:forEach>
	<p><a href="<s:url action="index" />">Back</a></p>
	   
</body>
</html>
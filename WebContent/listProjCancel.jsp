<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css">
<title>Cancel Project</title>
</head>
<body>
	<p>
		<a href="<s:url action="index" />">Back</a>
	</p>

	<c:if test="${session.username!= null}">
		<h3>
			<font color="black" id="username"><c:out
					value="Username: ${ session.username}" /></font>
		</h3>
	</c:if>
	<hr>
	
	<s:text name="List of projects" />

	<c:forEach items="${cancelProjectBean.projects}" var="value">
		<c:out value="${value}" />
		<br>
	</c:forEach>

	<s:text name="Cancel Projects" />
	<br>
	<s:form action="cancelProject" method="post">
	    ProjectID: 	<s:textfield name="cancelProjectBean.choice" type="string" />
		<s:submit value="Send" />
	</s:form>

</body>
</html>
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
		<h3>
			<font color="black" id="username"><c:out
					value="Username: ${ session.username}" /></font>
		</h3>
		<h3>
			<font color="black" id="money"><c:out
					value="Money: ${ session.clientMoney}" /></font>
		</h3>
	</c:if>
	<hr>

	<c:if test="${session.authErrorNC!= null}">
		<h3>
			<font color="red"><c:out value="${session.authErrorNC}" /></font>
		</h3>
		<c:remove var="authErrorNC" scope="session" />
	</c:if>
	<hr>


	<s:text name="Prime numbers needed?" />
	<s:form action="primes" method="post">
		<s:textfield name="primesBean.number" />
		<s:submit />
	</s:form>

	<c:if test="${session.username!= null}">

		<s:form action="availableProjects" method="get">
			<input type="submit" value="Show Available Projects">
		</s:form>
		
		<s:form action="olderProjects" method="get">
			<input type="submit" value="Show Older Projects">
		</s:form>

		<s:form action="projDetails" method="get">
			<s:textfield name="projectDetailsBean.choice" />
			<s:submit />
		</s:form>

		<s:form action="consultAccount" method="get">
			<input type="submit" value="Consult Account">
		</s:form>
		<s:form action="rewards" method="get">
			<input type="submit" value="Rewards">
		</s:form>
		<s:form action="donateMoney" method="get">
			<input type="submit" value="Donate Money">
		</s:form>
		
		<s:form action="sendMessage" method="get">
			<input type="submit" value="Send Message To Project">
		</s:form>
		<s:form action="createProject" method="get">
			<input type="submit" value="Create Project">
		</s:form>
		<s:form action="rewardsUpdate" method="get">
			<input type="submit" value="Add or Remove Rewards">
		</s:form>

		<s:form action="answerMessages" method="get">
			<input type="submit" value="Answer Messages">
		</s:form>

		<hr>
		<s:form action="logout" method="get">
			<input type="submit" value="logout">
		</s:form>

	</c:if>

	<c:if test="${session.username == null}">

		<s:form action="availableProjects" method="get">
			<input type="submit" value="Show Available Projects">
		</s:form>
		<s:form action="olderProjects" method="get">
			<input type="submit" value="Show Older Projects">
		</s:form>
		<s:form action="projDetails" method="get">
			<input type="submit" value="Show Project Details">
		</s:form>
		<s:form action="register" method="get">
			<input type="submit" value="Register">
		</s:form>
		<s:form action="login" method="get">
			<input type="submit" value="Login">
		</s:form>

	</c:if>



</body>
</html>
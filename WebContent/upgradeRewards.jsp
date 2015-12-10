<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css">
<title>Upgrade Rewards</title>
</head>
<body>
	
	<p><a href="<s:url action="index" />">Back</a></p>
	
	<c:if test="${session.username!= null}">
		<h3><font color="black" id="username"><c:out  value="Username: ${ session.username}"/></font></h3>
	</c:if>
	<hr>
	
	<!-- TODO  -->
	<s:text name="Add Reward" />
    <s:form action="addReward" method="post">
    <s:textfield name="rewardsAddBean.projectID" type="number" />
    <s:textfield name="rewardsAddBean.text" />
    <s:textfield name="rewardsAddBean.money" type="number" />
    <s:submit value="Add Reward"/>
	</s:form>    
	
	<s:text name="Remove Reward" />
    <s:form action="removeReward" method="post">
    <s:textfield name="rewardsRemoveBean.projectID" type="number" />
    <s:textfield name="rewardsRemoveBean.rewardID" type="number"/>
    <s:submit value="Remove Reward"/>
	</s:form>    
	
	
	
</body>
</html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css">
<title>Project creation</title>

 <link rel="stylesheet" type="text/css" href="/WebSocket/style.css">
    
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
	 
	<form action="createProjectSend" method="post">
	  Project Name: <s:textfield name="createProjectBean.name"/><br>
	  Description: <s:textfield name="createProjectBean.description"/><br>
	  Main Goal: <s:textfield name="createProjectBean.mainGoalString"/><br>
	  Deadline: <s:textfield name="createProjectBean.yearString" placeholder="Year"/>
	  			<s:textfield name="createProjectBean.monthString" placeholder="Month"/>
	  			<s:textfield name="createProjectBean.dayString" placeholder="Day"/><br>
	  RewardType: <s:textfield name ="createProjectBean.rewardType"/><br>
	  RewardValue: <s:textfield name ="createProjectBean.rewardsValue"/><br>
	  Question: <s:textfield name ="createProjectBean.question"/><br>
	  Choice1 : <s:textfield name ="createProjectBean.answers1"/><br>
	  Choice2 : <s:textfield name ="createProjectBean.answers2"/><br>
	 
	  <s:submit value="Create Project"/>
	</form>
	
	
</body>
</html>
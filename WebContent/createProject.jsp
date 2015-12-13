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
	 
	<form action="createProjectSend" method="post">
	  Project Name: <s:textfield name="createProjectBean.name"/><br>
	  Description: <s:textfield name="createProjectBean.description"/><br>
	  Main Goal: <s:textfield name="createProjectBean.mainGoalString"/><br>
	  Deadline: <s:textfield name="createProjectBean.yearString"/>
	  			<s:textfield name="createProjectBean.monthString"/>
	  			<s:textfield name="createProjectBean.dayString"/><br>
	  
	  
	 
	  <s:submit value="Create Project"/>
	</form>

<button id="reward" onclick="createReward()">New reward</button>
<button id="choice" onclick="createChoice()">New Choice</button>	
	
</body>
</html>
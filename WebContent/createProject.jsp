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
    

<script>
		var rewards = 1;
		var choices = 1;
		
    	function createReward(){
    		rewards++;
    		var f = document.getElementById('form');

    		f.appendChild(document.createElement('p'));
    		
    		var newReward = document.createElement("input");
    		
    		newReward.setAttribute('type', 'text');
    		newReward.setAttribute('placeholder', "Reward#"+rewards);
    		newReward.setAttribute('name', 'reward'+rewards);
    		
    		f.appendChild(newReward);
    		//paragrafo
    		f.appendChild(document.createElement('p'));
			
    		var newMoney = document.createElement("input");
    		
    		newMoney.setAttribute('type', 'number');
    		newMoney.setAttribute('placeholder', 'Money#'+rewards);
    		newMoney.setAttribute('name', 'money'+rewards);
    		
    		
    		
    		f.appendChild(newMoney);
    		
    		f.appendChild(document.createElement('p'));
    	}
    		
    	function createChoice(){
    		choices++;
    		var f = document.getElementById('form');

    		f.appendChild(document.createElement('p'));
    		
    		var newChoice = document.createElement("input");
    		
    		newChoice.setAttribute('type', 'text');
    		newChoice.setAttribute('placeholder', "Choice#"+choices);
    		newChoice.setAttribute('name', 'choice'+choices);
    		
    		f.appendChild(newChoice);
    		
    	}
    </script>
    
</head>
<body>
	 
	<form action="createProjectSend" method="post">
	  Project Name: <s:textfield name="createProjectBean.name"/><br>
	  Description: <s:textfield name="createProjectBean.description"/><br>
	  Goal: <s:number name="createProjectBean.mainGoal"/><br>
	  Deadline: <s:date name="createProjectBean.deadline"/><br>
	  Reward#1: <s:textfield name="createProjectBean.rewardsString"/> <br>
	  Money#1:<s:number name="createProjectBean.rewardsInt"/><br>
	  Question: <s:textfield name="createProjectBean.question"/><br>
	  Choice#1: <s:textfield name="createProjectBean.answers"/><br>
	  <s:submit value="Create Project"/>
	</form>

<button id="reward" onclick="createReward()">New reward</button>
<button id="choice" onclick="createChoice()">New Choice</button>	
	
</body>
</html>
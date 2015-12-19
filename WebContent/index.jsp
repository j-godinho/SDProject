<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css">
<title>SDProject</title>
<script type="text/javascript">

        var websocket = null;

        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/SDProjectGit/ws');
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
            	writeToNotifications('Get a real browser which supports WebSocket.', 0);
                return;
            }
        

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }
		
        function onOpen(event) {
        	writeToNotifications('Connected with webserver.', 0);
        }
        
        function onClose(event) {
        	writeToNotifications('WebSocket closed.' , 0);
        }
        
        var audio = new Audio('notificationSound.mp3');
        
        function onMessage(message) { 
        	var action = message.data.substring(0,3);
        	var messageToSend= message.data.substring(3,message.data.length);
        	
        	if(action==='[0]')
        		{
        			writeToNotifications(messageToSend, 1);
        		}
        	else if (action==='[1]')
        		{
        			projectMoneyUpdate(messageToSend);
        		}
        }
        
        function onError(event) {
        	writeToNotifications('WebSocket error (' + event.data + ').', 2);
        }
        
        

        function writeToNotifications(text, type) {
            var notifications = document.getElementById('notifications');
            var line = document.createElement('p');
            line.style.wordWrap = 'break-word';
            
            //som
            audio.play();
            
            line.innerHTML = text;
            
            //Status messages like connected, disconnected, error
            if(type == 0)
            	{
            		line.style.color = "Green";
            	}
            else if(type == 1)
            	{
            	line.style.color = "Blue";
	    		setTimeout(function(){
	   				line.style.color="Black";
	   			}, 5000);
            	}
            else{
            	line.style.color = "Red";
            }
            
			notifications.appendChild(line);
            notifications.scrollTop = notifications.scrollHeight;
            
        }
        
      	/*
	    //projectName
		//descriçao(String)
		//mainGoal(int)
		//deadLine
		//number of rewards-> 
		->dinheiro e reward para cada um 
		//question->escolher o numero de choices
		*/
        
        function projectMoneyUpdate(text)
        {
        	var projectMoney = document.getElementById('projectMoney');
        	projectMoney.innerHTML = text;
        	
        	
        }

    </script>
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

	<c:if test="${session.username!= null}">
		
		
		<s:form action="availableProjects" method="get">
			<input type="submit" value="Show Available Projects">
		</s:form>
		
		<s:form action="olderProjects" method="get">
			<input type="submit" value="Show Older Projects">
		</s:form>

		<s:form action="getProjects" method="get">
			<input type="submit" value="Project details">
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
		
		<s:form action="sendMessageList" method="get">
			<input type="submit" value="Send Message To Project">
		</s:form>
		<s:form action="createProject" method="get">
			<input type="submit" value="Create Project">
		</s:form>
		<s:form action="rewardsUpdate" method="get">
			<input type="submit" value="Add or Remove Rewards">
		</s:form>

		<s:form action="answerMessage" method="get">
			<input type="submit" value="Answer Messages">
		</s:form>
		
		<s:form action="listcancelProject" method="get">
			<input type="submit" value ="Cancel Project"/>
		</s:form>
		

		<hr>
		<s:form action="logout" method="get">
			<input type="submit" value="logout">
		</s:form>
		
		 <div id="container">
		<div id="notifications"></div>
	</div>
	
	<div id="projectMoney"></div>
	
	</c:if>

	<c:if test="${session.username == null}">

		<s:form action="availableProjects" method="get">
			<input type="submit" value="Show Available Projects">
		</s:form>
		<s:form action="olderProjects" method="get">
			<input type="submit" value="Show Older Projects">
		</s:form>
		
		<s:form action="getProjects" method="get">
			<input type="submit" value="Project details">
		</s:form>
		
		<s:form action="register" method="get">
			<input type="submit" value="Register">
		</s:form>
		<s:form action="login" method="get">
			<input type="submit" value="Login">
		</s:form>
		
		<s:form action="loginTumblr" method="get">
			<input type="submit" value="Login With Tumblr">
		</s:form>
		
		
	</c:if>
	


</body>

</html>
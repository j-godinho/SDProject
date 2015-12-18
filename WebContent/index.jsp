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

		<s:form action="projDetails" method="post">
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

		<s:form action="answerMessage" method="get">
			<input type="submit" value="Answer Messages">
		</s:form>
		
		<s:form action="chat" method="get">
			<input type="submit" value="Chat">
		</s:form>
		
		<s:form action="cancelProject" method="get">
			<s:textfield name="cancelProjectBean.choice" />
			<input type="submit" value ="Cancel Project"/>
		</s:form>
		

		<hr>
		<s:form action="logout" method="get">
			<input type="submit" value="logout">
		</s:form>
		
		<script type="text/javascript">

        var websocket = null;

        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/ws');
            document.getElementById("chat").focus();
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            writeToHistory('Connected to ' + window.location.host + '.');
            document.getElementById('chat').onkeydown = function(key) {
                if (key.keyCode == 13)
                    doSend(); // call doSend() on enter key
            };
        }
        
        function onClose(event) {
            writeToHistory('WebSocket closed.');
            document.getElementById('chat').onkeydown = null;
        }
        
        function onMessage(message) { // print the received message
            writeToHistory(message.data);
        }
        
        function onError(event) {
        	//writeToHistory('Connected to ' + window.location.host + '.')
            writeToHistory('WebSocket error (' + event.data + ').');
            document.getElementById('chat').onkeydown = null;
        }
        
        function doSend() {
            var message = document.getElementById('chat').value;
            if (message != '')
                websocket.send(message); // send the message
            document.getElementById('chat').value = '';
        }

        function writeToHistory(text) {
            var history = document.getElementById('history');
            var line = document.createElement('p');
            line.style.wordWrap = 'break-word';
            line.innerHTML = text;
            history.appendChild(line);
            history.scrollTop = history.scrollHeight;
        }

    </script>
	<div>
    <div id="container"><div id="history"></div></div>
    <p><input type="text" placeholder="type to chat" id="chat"></p>
	</div>
	</c:if>

	<c:if test="${session.username == null}">

		<s:form action="availableProjects" method="get">
			<input type="submit" value="Show Available Projects">
		</s:form>
		<s:form action="olderProjects" method="get">
			<input type="submit" value="Show Older Projects">
		</s:form>
		
		<s:form action="projDetails" method="post">
			<s:textfield name="projectDetailsBean.choice" />
			<s:submit />
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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>


<s:if test="#session.logged-in == 'false'">
	 <%
	 pageContext.forward("../login.jsp");
	 %>	
</s:if>

<s:if test="#session.logged-in == null">
	  <%
	  pageContext.forward("../login.jsp");
	  %>	
</s:if>

<s:if test="#session.usuario == null">
	   <%
	    pageContext.forward("../login.jsp");
	   %>	 
</s:if>

</body>
</html>
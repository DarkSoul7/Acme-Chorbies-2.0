<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${requestURI}" modelAttribute="eventForm">
	<form:hidden path="id"/>
	
	<acme:textbox code="event.title" path="title" mandatory="true" />
	
	<acme:datetimepicker code="event.moment" path="eventMoment" mandatory="true"/>
	
	<acme:textbox code="event.picture" path="picture" mandatory="true" />
	
	<acme:textbox code="event.seatsNumber" path="seatsNumber" mandatory="true" />
	
	<acme:textarea code="event.description" path="description" mandatory="true" />

	<acme:submit code="event.save" name="save"/>
	<acme:cancel code="event.back" url="/event/listOfManager.do"/>
		
</form:form>

<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<display:table name="events" id="row" requestURI="${requestURI}" pagesize="5">

	<spring:message code="event.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="event.moment" var="moment" />
	<display:column title="${moment}" >
		<jstl:choose>
			<jstl:when test="${cookie.language.value == 'en'}">
				<fmt:formatDate value="${row.eventMoment}" pattern="MM/dd/yyyy HH:mm" />
			</jstl:when>
			<jstl:otherwise>
				<fmt:formatDate value="${row.eventMoment}" pattern="dd/MM/yyyy HH:mm" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<spring:message code="event.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="event.picture" var="picture" />
	<display:column title="${picture}">
		<img src="${row.picture}" alt="picture" width="70" height="70">
	</display:column>
	
	<spring:message code="event.seatsNumber" var="seatsNumber"/>
	<display:column title="${seatsNumber}" sortable="true">
		<jstl:out value="${row.seatsNumber - fn:length(row.eventChorbies) }"></jstl:out>
	</display:column>
	
	<spring:message code="event.amount" var="amount" />
	<display:column property="amount" title="${amount}" />
	
	<display:column>
		<jstl:if test="${chorbiRegister}">
			<acme:cancel code="event.unregister" url="event/unregister.do?eventId=${row.id}" />
		</jstl:if>
	</display:column>
	<security:authorize access="hasRole('MANAGER')">
		<display:column>
			<acme:cancel code="event.sendChirbi" url="chirp/sendByManager.do?eventId=${row.id}" />
		</display:column>
		<display:column>
			<acme:cancel code="event.edit" url="event/edit.do?eventId=${row.id}" />
		</display:column>
		<display:column>
			<acme:confirm code="event.delete" url="event/delete.do?eventId=${row.id}" msg="chirp.confirmDeletion"/>
		</display:column>
	</security:authorize>
</display:table>

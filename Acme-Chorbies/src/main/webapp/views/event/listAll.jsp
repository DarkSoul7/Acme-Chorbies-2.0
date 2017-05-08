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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<display:table name="events" id="row" requestURI="${requestURI}"
	pagesize="5">

	<%-- 	<jstl:choose> --%>
	<jstl:if test="${row.highlighted == false }">
		<jstl:set var="style" value="background-color:gray" />
	</jstl:if>
	<jstl:if test="${row.highlighted == true}">
		<jstl:set var="style" value="background-color:yellow" />
	</jstl:if>
	<jstl:if test="${row.highlighted == null}">
		<jstl:set var="style" value="background-color:white" />
	</jstl:if>
	<%-- 		<jstl:when test="${not empty row.highlighted && row.highlighted}"> --%>
	<%-- 			<jstl:set var="style" value="background-color:gray" /> --%>
	<%-- 		</jstl:when> --%>
	<%-- 		<jstl:otherwise> --%>
	<%-- 			<jstl:set var="style" value="background-color:white" /> --%>
	<%-- 		</jstl:otherwise> --%>
	<%-- 	</jstl:choose> --%>

	<spring:message code="event.title" var="title" />
	<display:column style="${style}" property="title" title="${title}" />

	<spring:message code="event.moment" var="moment" />
	<display:column style="${style}" title="${moment}">
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
	<display:column style="${style}" property="description"
		title="${description}" />

	<spring:message code="event.picture" var="picture" />
	<display:column style="${style}" title="${picture}">
		<img src="${row.picture}" alt="picture" width="70" height="70">
	</display:column>

	<spring:message code="event.seatsNumber" var="seatsNumber" />
	<display:column style="${style}" 
		title="${seatsNumber}"  property="seatsAvailable" sortable="true"/>

	<security:authorize access="hasRole('CHORBI')">
		<display:column style="${style}">
			<jstl:if test="${!listChorbiJoinEventYet.contains(row.id)}">
<%-- 				<jstl:if test="${row.highlighted == true}"> --%>
<%-- 					<acme:cancel url="event/join.do?eventId=${row.id}" --%>
<%-- 						code="event.join" /> --%>
<%-- 				</jstl:if> --%>
				<jstl:if
					test="${(row.seatsAvailable > 0)}">
					<acme:cancel url="event/join.do?eventId=${row.id}"
						code="event.join" />
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>


</display:table>

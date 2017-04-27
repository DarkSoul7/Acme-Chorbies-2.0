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

<form:form action="${RequestURI}" modelAttribute="periodForm">
	
<%-- 	<form:hidden path="invoices" /> --%>
	
	<fieldset>
		<legend><spring:message code="periodForm.params"/></legend>
		
		<acme:datepicker code="periodForm.openPeriod" path="openPeriod" mandatory="false" />
		
		<acme:datepicker code="periodForm.endPeriod" path="endPeriod" mandatory="false" />
		<br/>
	</fieldset>	
	<br/>
	
	<acme:submit code="periodForm.save" name="save"/>
</form:form>

<display:table name="invoices" id="row" requestURI="${RequestURI}">

	
	<spring:message code="invoice.openPeriod" var="openPeriod" />
	<display:column title="${openPeriod}">
		<fmt:formatDate value="${row.openPeriod}" pattern="MM/dd/yyyy" />	
	</display:column>
	
	<spring:message code="invoice.endPeriod" var="endPeriod" />
	<display:column title="${endPeriod}">
		<fmt:formatDate value="${row.endPeriod}" pattern="MM/dd/yyyy" />	
	</display:column>	
	<spring:message code="invoice.amount" var="amount" />
	<display:column property="amount" title="${amount}" />
	
	<spring:message code="invoice.paid" var="paid" />
	<display:column title="${paid}">
		<jstl:if test="${row.paid == true}">
			<spring:message code="invoice.paidTrue" var="paidTrue" />
			<jstl:out value="${paidTrue}"></jstl:out>
		</jstl:if>
		<security:authorize access="hasRole('CHORBI')">
			<jstl:if test="${row.paid != true}">
				<acme:cancel code="invoice.pay" url="invoice/pay.do?invoiceId=${row.id}" />
			</jstl:if>
		</security:authorize>
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<jstl:if test="${row.paid != true}">
			<spring:message code="invoice.paidFalse" var="paidFalse" />
			<jstl:out value="${paidFalse}"></jstl:out>
		</jstl:if>
		</security:authorize>
	</display:column>
	
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<spring:message code="invoice.chorbi" var="chorbi" />
		<display:column property="chorbi.name" title="${chorbi}" />
	</security:authorize>
</display:table>

<jstl:if test="${errorMessage != null }">
	<spring:message code="invoice.list.error" var="error" />
	<font size="4" color="red"><jstl:out value="${error}"></jstl:out></font>
</jstl:if>

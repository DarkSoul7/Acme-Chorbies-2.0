<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="scripts/bootbox.min.js"></script>
<script type="text/javascript" src="scripts/acme.js"></script>

<link rel="stylesheet" href="styles/common.css" type="text/css">

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="/Acme-Chorbies"><img style="max-height: 150%;" src="images/logo-chorbies.jpg" alt="Acme-Chorbies Co., Inc." /></a>
		</div>
		<ul class="nav navbar-nav" style="margin:0; padding:0">
			<security:authorize access="hasRole('ADMINISTRATOR')">
				<li><a href="banner/list.do"><spring:message code="master.page.banner.list" /></a></li>
				<li><a href="searchTemplate/listCachedTime.do"><spring:message code="master.page.cachedTime.list" /></a></li>
				<li><a href="administrator/chorbi/list.do"><spring:message code="master.page.list.chorbi" /></a></li>
				<li><a href="fee/edit.do"><spring:message code="master.page.fee" /></a></li>
				<li><a href="invoice/listAll.do"><spring:message code="master.page.invoice" /></a></li>
				<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.administrator.dashboard" /><span class="caret"></span></a>
					<ul class="dropdown-menu inverse-dropdown">
						<li><a href="administrator/dashboardC.do"><spring:message code="master.page.administrator.dashboradC" /></a></li>
						<li><a href="administrator/dashboardB.do"><spring:message code="master.page.administrator.dashboradB" /></a></li>
						<li><a href="administrator/dashboardA.do"><spring:message code="master.page.administrator.dashboradA" /></a></li>
					</ul>
				</li>
			</security:authorize>
			
			<security:authorize access="hasRole('MANAGER')">
				<li><a href="event/listOfManager.do"><spring:message code="master.page.list.event.manager" /></a></li>
				<li><a href="event/register.do"><spring:message code="master.page.create.event.manager" /></a></li>
			</security:authorize>
			
			<security:authorize access="hasRole('CHORBI')">
				<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.chorbi" /><span class="caret"></span></a>
					<ul class="dropdown-menu inverse-dropdown">
						<li><a href="chorbi/list.do"><spring:message code="master.page.list.chorbi" /></a></li>
						<li><a href="chorbi/listLikeHim.do"><spring:message code="master.page.listLikeHim.chorbi" /></a></li>
					</ul>
				</li>
				<li><a href="searchTemplate/list.do"><spring:message code="master.page.searchTemplate" /></a></li>
				<li><a href="invoice/list.do"><spring:message code="master.page.invoice" /></a></li>
				<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.messagingSystem" /><span class="caret"></span></a>
					<ul class="dropdown-menu inverse-dropdown">
						<li><a href="chirp/send.do"><spring:message code="master.page.messagingSystem.send" /></a></li>
						<li><a href="chirp/sentChirps.do"><spring:message code="master.page.messagingSystem.sentChirps" /></a></li>
						<li><a href="chirp/receivedChirps.do"><spring:message code="master.page.messagingSystem.receivedChirps" /></a></li>
					</ul>
				</li>
				<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.event" /><span class="caret"></span></a>
						<ul class="dropdown-menu inverse-dropdown">
							<li><a href="event/listRegister.do"><spring:message code="master.page.event.listRegister" /></a></li>
							<li><a href="event/listAll.do"><spring:message code="master.page.event.listAll" /></a></li>
						</ul>
					</li>
			</security:authorize>
		</ul>
		
		<div>
			<div class="nav navbar-nav navbar-right" style="padding-top:8px; padding-right:6px; padding-left:6px;">
				<a href="?language=es"><img src="images/es.svg" style="height: 15px;" /></a>
				<span style="border-top: 6px solid #333; height: 1px;display: block;"></span>
				<a href="?language=en"><img src="images/us.svg" style="height: 15px;" /></a>
			</div>
			<security:authorize access="isAnonymous()">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="chorbi/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.chorbi.register" /></a></li>
					<li><a href="security/login.do"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="master.page.login" /></a></li>
					<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.event" /><span class="caret"></span></a>
						<ul class="dropdown-menu inverse-dropdown">
							<li><a href="event/list.do"><spring:message code="master.page.event.list" /></a></li>
							<li><a href="event/listAll.do"><spring:message code="master.page.event.listAll" /></a></li>
						</ul>
					</li>
				</ul>
			</security:authorize>
			<security:authorize access="isAuthenticated()">
				<div class="nav navbar-nav navbar-right">
					<ul class="nav navbar-nav">
						<li class="dropdown"><a class="handCursor dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.profile" /> (<security:authentication property="principal.username" />)<span class="caret"></span></a>
							<ul class="dropdown-menu inverse-dropdown">
								<security:authorize access="hasRole('CHORBI')">
									<li><a href="chorbi/edit.do"><span class="glyphicon glyphicon-edit"></span> <spring:message code="master.page.edit.chorbi" /></a></li>	
								</security:authorize>
								<li><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /> </a></li>
							</ul>
						</li>
					</ul>
				</div>
			</security:authorize>
		</div>
	</div>
</div>

<br/>
<br/>
<br/>

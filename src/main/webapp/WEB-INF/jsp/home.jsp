<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="home.title">
  <c:if test="${error}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="home.error"/>
      </h1>
    </div>
  </c:if>
  <c:if test="${ipRestrict}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="home.ip.error"/>
      </h1>
    </div>
  </c:if>
  <c:if test="${employeeNonAssigned}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="home.employee.nonAssigned.error"/>
      </h1>
    </div>
  </c:if>
  <h1 class="h3 mb-3 font-weight-normal">
    <spring:message code="home.welcome"/>
  </h1>

  <c:if test="${not isLoggedIn}">
    <a class="btn btn-lg btn-primary btn-block" href="/login">
      <spring:message code="home.login"/>
    </a>
    <a class="btn btn-lg btn-primary btn-block" href="/register">
      <spring:message code="home.register"/>
    </a>
  </c:if>
 </t:page>
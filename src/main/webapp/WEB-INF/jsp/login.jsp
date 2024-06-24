<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="login.title">
<form class="form-signin text-center" action="/perform_login" method='POST'>
  <c:if test="${error}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="login.error"/>
      </h1>
    </div>
  </c:if>
  <c:if test="${ipRestrict}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="login.ip.error"/>
      </h1>
    </div>
  </c:if>
  <c:if test="${employeeNonAssigned}">
    <div class="alert alert-danger" role="alert">
      <h1 class="font-weight-normal">
        <spring:message code="login.employee.nonAssigned.error"/>
      </h1>
    </div>
  </c:if>
  <h1 class="h3 mb-3 font-weight-normal">
    <spring:message code="login.welcome"/>
  </h1>
  <div class="form-group">
  <label for="username" class="sr-only">
    <spring:message code="login.form.email"/>
  </label>
  <input type="email" id="username" name="username" class="form-control" placeholder="<spring:message code="login.form.email"/>" required autofocus>
  </div>
  <div class="form-group">
  <label for="password" class="sr-only">
    <spring:message code="login.form.password"/>
  </label>
  <input type="password" id="password" name="password" class="form-control" placeholder="<spring:message code="login.form.password"/>" required>
  </div>
  <button class="btn btn-lg btn-primary btn-block" type="submit">
    <spring:message code="login.form.button"/>
  </button>
  <a class="btn btn-lg btn-primary btn-block" href="/register">
    <spring:message code="login.form.register"/>
  </a>
</form>
  </t:page>
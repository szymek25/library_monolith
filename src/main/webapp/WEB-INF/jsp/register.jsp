<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="register.title">

  <form:form modelAttribute="registerForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="register.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputEmail">
          <spring:message code="register.form.email"/>
        </label>
        <form:input type="email" path="email" class="form-control" id="inputEmail"/>
        <form:errors path="email" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputPassword">
          <spring:message code="register.form.password"/>
        </label>
        <form:input type="password" path="password" class="form-control" id="inputPassword"/>
        <form:errors path="password" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputConfirmPassword">
          <spring:message code="register.form.confirmPassword"/>
        </label>
        <form:input type="password" path="confirmPassword" class="form-control"
                    id="inputConfirmPassword"/>
        <form:errors path="confirmPassword" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-4">
        <label for="inputName">
          <spring:message code="register.form.name"/>
        </label>
        <form:input type="text" path="name" class="form-control" id="inputName"/>
        <form:errors path="name" cssClass="error"/>
      </div>
      <div class="form-group col-md-4">
        <label for="lastNameInput">
          <spring:message code="register.form.lastName"/>
        </label>
        <form:input type="text" path="lastName" class="form-control" id="lastNameInput"/>
        <form:errors path="lastName" cssClass="error"/>
      </div>
      <div class="form-group col-md-4">
        <label for="dayOfBirthInput">
          <spring:message code="register.form.dayOfBirth"/>
        </label>
        <form:input type="date" path="dayOfBirth" class="form-control" id="dayOfBirthInput"/>
        <form:errors path="dayOfBirth" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-4">
        <label for="inputAddressLine1">
          <spring:message code="register.form.addressLine1"/>
        </label>
        <form:input type="text" path="addressLine1" class="form-control" id="inputAddressLine1"/>
        <form:errors path="addressLine1" cssClass="error"/>
      </div>
      <div class="form-group col-md-4">
        <label for="townInput">
          <spring:message code="register.form.town"/>
        </label>
        <form:input type="text" path="town" class="form-control" id="townInput"/>
        <form:errors path="town" cssClass="error"/>
      </div>
      <div class="form-group col-md-4">
        <label for="postalCodeInput">
          <spring:message code="register.form.postalCode"/>
        </label>
        <form:input type="text" path="postalCode" class="form-control" id="postalCodeInput"/>
        <form:errors path="postalCode" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="phoneInput">
          <spring:message code="register.form.phone"/>
        </label>
        <form:input type="phone" path="phone" class="form-control" id="phoneInput"/>
        <form:errors path="phone" cssClass="error"/>
      </div>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="register.form.submit"/>
    </button>
  </form:form>

</t:page>
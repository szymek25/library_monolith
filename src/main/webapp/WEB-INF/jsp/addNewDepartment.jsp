<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="departments.add.title">

  <form:form modelAttribute="addDepartmentForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="departments.add.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputName">
          <spring:message code="departments.add.form.name"/>
        </label>
        <form:input type="text" path="name" class="form-control" id="inputName"/>
        <form:errors path="name" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputTown">
          <spring:message code="departments.add.form.town"/>
        </label>
        <form:input type="text" path="town" class="form-control" id="inputTown"/>
        <form:errors path="town" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputAddressLine1">
          <spring:message code="departments.add.form.addressLine1"/>
        </label>
        <form:input type="text" path="addressLine1" class="form-control" id="inputAddressLine1"/>
        <form:errors path="addressLine1" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputPostalCode">
          <spring:message code="departments.add.form.postalCode"/>
        </label>
        <form:input type="text" path="postalCode" class="form-control" id="inputpostalCode"/>
        <form:errors path="postalCode" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputAddressIp">
          <spring:message code="departments.add.form.ipAddress"/>
        </label>
        <form:input type="text" path="ipAddress" class="form-control" id="inputAddressIp"/>
        <form:errors path="ipAddress" cssClass="error"/>
      </div>
    </div>


    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="departments.add.form.submit"/>
    </button>
  </form:form>
</t:page>
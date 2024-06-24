<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="categories.add.title">

  <form:form modelAttribute="addCategoryForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="categories.add.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputName">
          <spring:message code="categories.add.form.name"/>
        </label>
        <form:input type="text" path="name" class="form-control" id="inputName"/>
        <form:errors path="name" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputDaysOfRent">
          <spring:message code="categories.add.form.daysOfRent"/>
        </label>
        <form:input type="number" path="daysOfRent" class="form-control" id="inputDaysOfRent"/>
        <form:errors path="daysOfRent" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputProlongation">
          <spring:message code="categories.add.form.continuationPossible"/>
        </label>
        <form:checkbox path="continuationPossible" class="" id="inputProlongation"/>
        <form:errors path="continuationPossible" cssClass="error"/>
      </div>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="categories.add.form.submit"/>
    </button>
  </form:form>

</t:page>
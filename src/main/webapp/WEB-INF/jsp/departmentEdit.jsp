<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="department.edit.title">

  <form:form modelAttribute="editDepartmentForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="department.edit.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputName">
          <spring:message code="department.edit.form.name"/>
        </label>
        <form:input type="text" path="name" class="form-control" id="inputName"/>
        <form:errors path="name" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputTown">
          <spring:message code="department.edit.form.town"/>
        </label>
        <form:input type="text" path="town" class="form-control" id="inputTown"/>
        <form:errors path="town" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputAddressLine1">
          <spring:message code="department.edit.form.addressLine1"/>
        </label>
        <form:input type="text" path="addressLine1" class="form-control" id="inputAddressLine1"/>
        <form:errors path="addressLine1" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputPostalCode">
          <spring:message code="department.edit.form.postalCode"/>
        </label>
        <form:input type="text" path="postalCode" class="form-control" id="inputPostalCode"/>
        <form:errors path="postalCode" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputIpAddress">
          <spring:message code="department.edit.form.ipaddress"/>
        </label>
        <form:input type="text" path="ipAddress" class="form-control" id="inputIpAddress"/>
        <form:errors path="ipAddress" cssClass="error"/>
      </div>
    </div>

    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="department.edit.form.submit"/>
    </button>
  </form:form>

  <div class="row">
    <div class="col">
      <h1><spring:message code="department.edit.employees.title"/></h1>
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">
            <spring:message code="department.edit.employees.table.email"/>
          </th>
          <th scope="col">
            <spring:message code="department.edit.employees.table.name"/>
          </th>
          <th scope="col">
            <spring:message code="department.edit.employees.table.lastName"/>
          </th>
          <th scope="col">
            <spring:message code="department.edit.employees.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${department.employees}" var="employee">
          <tr>
            <th scope="row">${employee.id}</th>
            <td>${employee.email}</td>
            <td>${employee.name}</td>
            <td>${employee.lastName}</td>
            <td>
              <form action="/departments/removeEmployee" method="post">
                <input type="hidden" name="userId" value="${employee.id}"/>
                <input type="hidden" name="departmentId" value="${department.id}"/>
                <button type="submit" class="btn btn-primary">
                  <spring:message code="department.edit.employees.table.actions.delete"/>
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
  <a href="/departments/selectEmployee/${department.id}" class="btn btn-primary">
    <spring:message code="department.edit.employees.add"/>
  </a>

</t:page>
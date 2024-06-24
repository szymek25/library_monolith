<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="departments.employees.title">
  <div class="row">
    <div class"col">
      <h2>
        <spring:message code="departments.employees.department.head"/>
      </h2>
      <p>
        <spring:message code="departments.employees.department.name"/>: ${department.name}
      </p>
      <p>
        <spring:message code="departments.employees.department.addressLine1"/>: ${department.addressLine1}
      </p>
      <p>
        <spring:message code="departments.employees.department.postalCode"/>: ${department.postalCode}
      </p>
      <p>
        <spring:message code="departments.employees.department.town"/>: ${department.town}
      </p>
    <div>
  </div>

  <div class="row">
    <div class="col">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">
            <spring:message code="departments.employees.list.table.email"/>
          </th>
          <th scope="col">
            <spring:message code="departments.employees.list.table.name"/>
          </th>
          <th scope="col">
            <spring:message code="departments.employees.list.table.lastName"/>
          </th>
          <th scope="col">
            <spring:message code="departments.employees.list.table.dayOfBirth"/>
          </th>
          <th scope="col">
            <spring:message code="departments.employees.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${users}" var="user">
          <tr>
            <th scope="row">${user.id}</th>
            <td>${user.email}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.dayOfBirth}</td>
            <td>
              <form action="/departments/selectEmployee" method="POST">
                <input type="hidden" name="userId" value="${user.id}"/>
                <input type="hidden" name="departmentId" value="${department.id}"/>
                <button class="btn btn-primary btn-block" type="submit">
                  <spring:message code="departments.employees.list.table.actions.select"/>
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <t:paginator pages="${pages}" current="${currentPage}"/>
    </div>
  </div>
</t:page>
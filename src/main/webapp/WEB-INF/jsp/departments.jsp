<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="departments.list.title">

  <div class="row">
    <div class="col">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">
            <spring:message code="departments.list.table.name"/>
          </th>
          <th scope="col">
            <spring:message code="departments.list.table.address"/>
          </th>
          <th scope="col">
            <spring:message code="departments.list.table.town"/>
          </th>
          <th scope="col">
            <spring:message code="departments.list.table.postalCode"/>
          </th>
          <th scope="col">
            <spring:message code="departments.list.table.ipaddress"/>
          </th>
          <th scope="col">
            <spring:message code="departments.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${departments}" var="department">
          <tr>
            <th scope="row">${department.id}</th>
            <td>${department.name}</td>
            <td>${department.addressLine1}</td>
            <td>${department.town}</td>
            <td>${department.postalCode}</td>
            <td>${department.ipAddress}</td>
            <td>
              <a class="btn btn-primary btn-block" href="edit/${department.id}">
                <spring:message code="departments.list.table.actions.edit"/>
              </a>
              <form action="delete/${department.id}" method="POST">
                <button class="btn btn-primary btn-block" type="submit">
                  <spring:message code="departments.list.table.actions.delete"/>
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
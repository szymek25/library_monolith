<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="users.list.title">


  <div class="row">
    <div class="col find-by-barcode-box">
      <p> <spring:message code="users.list.findUser.info"/> </p>
      <form action="#" method="POST">
        <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="users.list.findUser.input.placeholder"/>"/>
      </form>
    </div>
  </div>
  <c:choose>
    <c:when test="${empty users}">
      <spring:message code="users.list.table.empty"/>
    </c:when>
    <c:otherwise>
      <div class="row">
        <div class="col">
          <table class="table">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">
                <spring:message code="users.list.table.email"/>
              </th>
              <th scope="col">
                <spring:message code="users.list.table.name"/>
              </th>
              <th scope="col">
                <spring:message code="users.list.table.lastName"/>
              </th>
              <th scope="col">
                <spring:message code="users.list.table.dayOfBirth"/>
              </th>
              <sec:authorize access="hasRole('MANAGER')">
                <th scope="col">
                  <spring:message code="users.list.table.type"/>
                </th>
              </sec:authorize>
              <th scope="col">
                <spring:message code="users.list.table.actions"/>
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
                <sec:authorize access="hasRole('MANAGER')">
                  <td>${user.accountType}</td>
                </sec:authorize>
                <td>
                  <c:set var="userId" value="${user.id}"/>
                  <sec:authorize access="hasRole('EMPLOYEE')">
                    <a target="_blank" href="/users/printUserLabel/${userId}" class="btn btn-primary btn-block">
                      <spring:message code="users.list.table.actions.print"/>
                    </a>
                    <a class="btn btn-primary btn-block" href="edit/${userId}">
                      <spring:message code="users.list.table.actions.edit"/>
                    </a>
                  </sec:authorize>
                  <sec:authorize access="hasRole('MANAGER')">
                    <a class="btn btn-primary btn-block" href="edit/${userId}">
                      <spring:message code="users.list.table.actions.edit"/>
                    </a>
                    <form action="delete/${userId}" method="POST">
                      <button class="btn btn-primary btn-block" type="submit">
                        <spring:message code="users.list.table.actions.delete"/>
                      </button>
                    </form>
                  </sec:authorize>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <t:paginator pages="${pages}" current="${currentPage}"/>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</t:page>
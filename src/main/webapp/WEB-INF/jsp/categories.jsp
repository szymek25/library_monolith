<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="categories.list.title">


  <div class="row">
    <div class="col">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">
            <spring:message code="categories.list.table.name"/>
          </th>
          <th scope="col">
            <spring:message code="categories.list.table.daysOfRent"/>
          </th>
          <th scope="col">
            <spring:message code="categories.list.table.continuationPossible"/>
          </th>
          <th scope="col">
            <spring:message code="categories.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${categories}" var="category">
          <tr>
            <th scope="row">${category.id}</th>
            <td>${category.name}</td>
            <td>${category.daysOfRent}</td>
            <td>
              <c:if test="${not empty category.continuationPossible}">
                <spring:message code="categories.list.table.continuationPossible.${category.continuationPossible}"/>
              </c:if>
            </td>
            <td>
              <a class="btn btn-primary btn-block" href="edit/${category.id}">
                <spring:message code="categories.list.table.actions.edit"/>
              </a>
              <c:if test="${!category.hasBooks}">
                <form action="delete/${category.id}" method="POST">
                  <button class="btn btn-primary btn-block" type="submit">
                    <spring:message code="categories.list.table.actions.delete"/>
                  </button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <t:paginator pages="${pages}" current="${currentPage}"/>
    </div>
  </div>
</t:page>
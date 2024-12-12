<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="rents.users.title">
  <c:set var="book" value="${bookEntry.book}"/>
  <div class="row">
    <div class"col">
      <h2>
        <spring:message code="rents.users.book.head"/>
      </h2>
      <p>
        <spring:message code="rents.users.book.title"/>: ${book.title}
      </p>
      <p>
        <spring:message code="rents.users.book.signature"/>: ${bookEntry.signature}
      </p>
      <p>
        <spring:message code="rents.users.book.inventoryNumber"/>: ${bookEntry.inventoryNumber}
      </p>
      <p>
        <spring:message code="rents.users.book.isbn"/>: ${book.isbn}
      </p>
      <p>
        <spring:message code="rents.users.book.author"/>: ${book.author}
      </p>
      <p>
        <spring:message code="rents.users.book.publicationYear"/>: ${book.publicationYear}
      </p>
    <div>
  </div>

  <div class="row">
    <div class="col find-by-barcode-box">
      <p> <spring:message code="rents.users.selectUser.scan.info"/> </p>
      <form action="/rents/createRentByBarcode" method="POST">
        <input type="hidden" name="bookEntryId" value="${bookEntry.id}"/>
        <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="rents.users.selectUser.scan.input.placeholder"/>"/>
      </form>
    </div>
  </div>
  <c:choose>
    <c:when test="${empty users}">
      <spring:message code="rents.users.list.table.empty"/>
    </c:when>
    <c:otherwise>
      <div class="row">
        <div class="col">
          <table class="table">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">
                <spring:message code="rents.users.list.table.email"/>
              </th>
              <th scope="col">
                <spring:message code="rents.users.list.table.name"/>
              </th>
              <th scope="col">
                <spring:message code="rents.users.list.table.lastName"/>
              </th>
              <th scope="col">
                <spring:message code="rents.users.list.table.dayOfBirth"/>
              </th>
              <th scope="col">
                <spring:message code="rents.users.list.table.actions"/>
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
                  <form action="/rents/createRent" method="POST">
                    <input type="hidden" name="userId" value="${user.id}"/>
                    <input type="hidden" name="bookEntryId" value="${bookEntry.id}"/>
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="rents.users.list.table.actions.select"/>
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
    </c:otherwise>
  </c:choose>
</t:page>
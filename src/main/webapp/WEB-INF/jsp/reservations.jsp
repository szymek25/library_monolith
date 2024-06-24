<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<t:page title="reservations.list.title">

  <sec:authorize access="hasRole('EMPLOYEE')">
    <div class="row">
      <div class="col">
        <div class="col find-by-barcode-box">
          <p> <spring:message code="reservations.list.findByUser.info"/> </p>
          <form action="/reservations/reservationsForUser" method="GET">
            <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="reservations.list.findByUser.input.placeholder"/>"/>
          </form>
        </div>
      </div>
    </div>
  </sec:authorize>

  <div class="row">
    <div class="col">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">
            <spring:message code="reservations.list.table.userEmail"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.bookTitle"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.bookInventoryNumber"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.reservationDate"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.department"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.queueNo"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.predictedTimeForCollect"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.status"/>
          </th>
          <th scope="col">
            <spring:message code="reservations.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${reservations}" var="reservation">
          <tr>
            <th scope="row">${reservation.id}</th>
            <td>${reservation.userEmail}</td>
            <td>${reservation.bookTitle}</td>
            <td>${reservation.bookInventoryNumber}</td>
            <td>${reservation.reservationDate}</td>
            <td>${reservation.departmentName}</td>
            <td>${reservation.queueNo}</td>
            <td>${reservation.predictedTimeForCollect}</td>
            <td>
              <c:if test="${not empty reservation.status}">
                <spring:message code="reservations.status.${reservation.status}"/>
              </c:if>
            </td>
            <td>
              <c:set var="status" value="${reservation.status}"/>
              <sec:authorize access="hasRole('EMPLOYEE') && ${reservation.currentEmployeeHasPermissionToBook}">
                <c:if test="${status eq 'READY_FOR_COLLECT'}">
                  <form action="/reservations/finish/${reservation.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="reservations.list.table.actions.finish"/>
                    </button>
                  </form>
                </c:if>
              </sec:authorize>
              <sec:authorize access="hasRole('USER')">
                <c:if test="${status ne 'CANCELED' && status ne 'PERFORMED'}">
                  <form action="/reservations/cancel/${reservation.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="reservations.list.table.actions.cancel"/>
                    </button>
                  </form>
                </c:if>
              </sec:authorize>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <t:paginator pages="${pages}" current="${currentPage}"/>
    </div>
  </div>
</t:page>
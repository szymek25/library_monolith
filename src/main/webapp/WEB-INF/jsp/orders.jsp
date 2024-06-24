<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="orders.list.title">

  <sec:authorize access="hasRole('EMPLOYEE')">
    <div class="row">
      <div class="col">
        <div class="col find-by-barcode-box">
          <p> <spring:message code="orders.list.findByUser.info"/> </p>
          <form action="/orders/ordersForUser" method="GET">
            <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="orders.list.findByUser.input.placeholder"/>"/>
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
            <spring:message code="orders.list.table.userEmail"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.bookTitle"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.inventoryNumber"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.created"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.department"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.status"/>
          </th>
          <th scope="col">
            <spring:message code="orders.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${orders}" var="order">
          <tr>
            <th scope="row">${order.id}</th>
            <td>${order.userEmail}</td>
            <td>${order.bookTitle}</td>
            <td>${order.bookInventoryNumber}</td>
            <td>${order.created}</td>
            <td>${order.departmentName}</td>
            <td>
              <c:if test="${not empty order.status}">
                <spring:message code="order.status.${order.status}"/>
              </c:if>
            </td>
            <td>
              <c:set var="status" value="${order.status}"/>
              <sec:authorize access="hasRole('EMPLOYEE') && ${order.currentEmployeeHasPermissionToBook}">
                <c:if test="${order.status ne 'CANCELED' && status ne 'FINISHED'}">
                  <form action="/orders/cancel/${order.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="orders.list.table.actions.cancel"/>
                    </button>
                  </form>
                </c:if>
                <c:if test="${order.status eq 'NEW'}">
                  <form action="/orders/confirm/${order.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="orders.list.table.actions.confirm"/>
                    </button>
                  </form>
                </c:if>
                <c:if test="${order.status eq 'CONFIRMED'}">
                  <form action="/orders/finish/${order.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="orders.list.table.actions.finish"/>
                    </button>
                  </form>
                </c:if>
              </sec:authorize>
              <sec:authorize access="hasRole('USER')">
                <c:if test="${status ne 'CANCELED' && status ne 'FINISHED'}">
                  <form action="/orders/cancel/${order.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="orders.list.table.actions.cancel"/>
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
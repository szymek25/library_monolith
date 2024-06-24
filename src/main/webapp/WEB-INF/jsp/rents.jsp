<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="rents.list.title">

  <c:if test="${success eq 'true'}">
    <div class="alert alert-success" role="alert">
      <spring:message code="rents.list.createRent.success"/>
    </div>
  </c:if>
  <c:if test="${success eq 'false'}">
     <div class="alert alert-danger" role="alert">
      <spring:message code="rents.list.createRent.failed"/>
     </div>
   </c:if>

  <sec:authorize access="hasRole('EMPLOYEE')">
    <div class="row">
      <div class="col">
        <div class="col find-by-barcode-box">
          <p> <spring:message code="rents.list.findByUser.info"/> </p>
          <form action="/rents/rentsForUser" method="GET">
            <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="rents.list.findByUser.input.placeholder"/>"/>
          </form>
        </div>
      </div>
      <div class="col">
        <div class="col find-by-barcode-box">
          <p> <spring:message code="rents.list.findByBook.info"/> </p>
          <form action="/rents/rentsForBook" method="GET">
            <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="rents.list.findByBook.input.placeholder"/>"/>
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
            <spring:message code="rents.list.table.userEmail"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.bookTitle"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.inventoryNumber"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.bookIsbn"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.startDate"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.endDate"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.returnDate"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.department"/>
          </th>
          <th scope="col">
            <spring:message code="rents.list.table.actions"/>
          </th>
        </tr>
        </thead>
        <c:forEach items="${rents}" var="rent">
          <tr>
            <th scope="row">${rent.id}</th>
            <td>${rent.userEmail}</td>
            <td>${rent.bookTitle}</td>
            <td>${rent.bookInventoryNumber}</td>
            <td>${rent.bookIsbn}</td>
            <td>${rent.startDate}</td>
            <td>${rent.endDate}</td>
            <td>${rent.returnDate}</td>
            <td>${rent.departmentName}</td>
            <td>
              <sec:authorize access="hasRole('EMPLOYEE') && ${rent.currentEmployeeHasPermissionToBook}">
                <c:if test="${empty rent.returnDate}">
                  <button class="btn btn-primary btn-block end-rent-button" data-rentId="${rent.id}"
                  data-physicalState="${rent.bookPhysicalState}"
                  data-bookTitle="${rent.bookTitle}">
                    <spring:message code="rents.list.table.actions.end"/>
                  </button>
                </c:if>
              </sec:authorize>
              <sec:authorize access="hasRole('USER') && ${rent.prolongationPossible}">
                <form action="/rents/prolongation" method="POST">
                  <input type="hidden" name="rentId" value="${rent.id}"/>
                  <button class="btn btn-primary btn-block" type="submit">
                    <spring:message code="rents.list.table.actions.prolongation"/>
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

  <div class="modal fade" id="rentEndingModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel"><spring:message code="rents.list.ending.modal.title"/></h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
          <b> <spring:message code="rents.list.ending.modal.bookTitle"/>: </b>
          <a id="rent-modal-book-title"> </a>
          <p>
            <b> <spring:message code="rents.list.ending.modal.bookPhysicalState"/>: </b>
            <a id="rent-modal-book-physical-state"> </a>
          </p>

          <form id="endRentForm" action="/rents/end" method="POST">
            <p class="udpate-physical-state-info"> <spring:message code="rents.list.ending.modal.bookPhysicalState.update"/> </p>
            <a class="btn btn-primary btn-block udpate-physical-state">
              <spring:message code="rents.list.ending.modal.bookPhysicalState.update.button"/>
            </a>
            <input type="hidden" name="rentId" id="rentId"/>
            <div class="update-physical-state-block display-none">
              <label for="physicalStateInput">
                <spring:message code="rents.list.ending.modal.bookPhysicalState.update.input"/>
              </label>
              <input class="form-control" type="text" name="physicalState" id="physicalStateInput"/>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">
            <spring:message code="rents.list.ending.modal.cancel"/>
          </button>
          <button id="confirmEndRentButton" type="button" class="btn btn-primary">
            <spring:message code="rents.list.ending.modal.confirm"/>
          </button>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" class="rentEndingModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel"><spring:message code="rents.list.ending.modal.title"/></h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">

        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">
            <spring:message code="rents.list.ending.modal.cancel"/>
          </button>
          <button id="confirmRemoveBookEntry" type="button" class="btn btn-primary">
            <spring:message code="rents.list.ending.modal.confirm"/>
          </button>
        </div>
      </div>
    </div>
  </div>
</t:page>
<%@ attribute name="entry" required="true" type="pl.szymanski.springfrontend.dtos.BookEntryDTO" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card">
 <div class="card-body">
   <p>
     <b><spring:message code="books.detail.entries.inventoryNumber"/>: </b> ${entry.inventoryNumber}
   </p>
   <p>
     <b><spring:message code="books.detail.entries.signature"/>: </b>${entry.signature}
   </p>
   <p>
     <b><spring:message code="books.detail.entries.physicalState"/>: </b>${entry.physicalState}
   </p>
   <p>
     <b><spring:message code="books.detail.entries.departmentName"/>: </b>
     <a href="javascript:void(0);" class="department-name" data-departmentid="${entry.departmentId}">${entry.departmentName}</a>
   </p>
   <c:choose>
     <c:when test="${entry.isBookRented}">
       <p class="no-available">
         <spring:message code="books.detail.entries.noavailable"/>
         <i class="fa fa-times" aria-hidden="true"></i>
       </p>
     </c:when>
     <c:otherwise>
       <p class="available">
         <spring:message code="books.detail.entries.available"/>
         <i class="fa fa-check" aria-hidden="true"></i>
       </p>
     </c:otherwise>
   </c:choose>
   <div class="row book-entry-actions">
     <sec:authorize access="hasRole('EMPLOYEE') && ${entry.currentEmployeeHasPermissionToBook}">
       <button type="button" class="btn btn-primary book-entry-edit"
       data-id="${entry.id}" data-inventoryNumber="${entry.inventoryNumber}" data-signature="${entry.signature}" data-physicalState="${entry.physicalState}">
         <spring:message code="books.detail.entries.edit"/>
       </button>
       <c:if test="${!entry.isBookRented}">
         <button type="button" class="btn btn-primary book-entry-remove" data-id="${entry.id}">
           <spring:message code="books.detail.entries.delete"/>
         </button>
         <a href="/rents/selectUser/${entry.id}" class="btn btn-primary">
           <spring:message code="books.detail.entries.rent"/>
         </a>
       </c:if>
       <a target="_blank" href="/books/printBookLabel/${entry.id}" class="btn btn-primary">
         <spring:message code="books.detail.entries.printLabel"/>
       </a>
     </sec:authorize>
     <sec:authorize access="hasRole('USER')">
       <c:choose>
         <c:when test="${!entry.isBookRented}">
           <form action="/orders/createOrder" method="POST">
             <input type="hidden" name="bookEntryId" value="${entry.id}"/>
             <button class="btn btn-primary btn-block" type="submit">
               <spring:message code="books.detail.entries.order"/>
             </button>
           </form>
         </c:when>
         <c:when test="${entry.isBookRented && !entry.userReservedBook && !entry.userOrderedBook}">
           <form action="/reservations/createReservation" method="POST">
             <input type="hidden" name="bookEntryId" value="${entry.id}"/>
             <button class="btn btn-primary btn-block" type="submit">
               <spring:message code="books.detail.entries.reservation"/>
             </button>
           </form>
         </c:when>
         <c:when test="${entry.userReservedBook}">
           <p><spring:message code="books.details.entries.bookIsReserved"/></p>
         </c:when>
         <c:when test="${entry.userOrderedBook}">
           <p><spring:message code="books.details.entries.bookIsOrdered"/></p>
         </c:when>
       </c:choose>
     </sec:authorize>
   </div>
 </div>
</div>
<!-- Modal -->
<div class="modal fade" id="departmentInfoModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title department-modal-title" id="exampleModalLabel"><spring:message code="books.detail.entries.departmentmodal.title"/></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div id="departmentInfoModalBody">

      </div>
    </div>
  </div>
</div>
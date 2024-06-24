<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="books.detail.title">

  <c:if test="${success eq 'true'}">
    <div class="alert alert-success" role="alert">
      <spring:message code="books.detail.entries.success"/>
    </div>
  </c:if>
  <c:if test="${success eq 'false'}">
     <div class="alert alert-danger" role="alert">
      <spring:message code="books.detail.entries.failed"/>
     </div>
   </c:if>
  <div class="book-info col-md-6">
    <p><spring:message code="books.detail.book.isbn"/>: ${book.isbn} </p>
    <p><spring:message code="books.detail.book.title"/>: ${book.title} </p>
    <p><spring:message code="books.detail.book.author"/>: ${book.author} </p>
    <p><spring:message code="books.detail.book.publicationYear"/>: ${book.publicationYear} </p>
    <p><spring:message code="books.detail.book.publisher"/>: ${book.publisher} </p>
  </div>

  <sec:authorize access="hasRole('EMPLOYEE')">
    <button id="add-book-entry-button" class="btn btn-lg btn-primary btn-block col-md-6">
      <spring:message code="books.detail.entryform.show"/>
    </button>

    <div class="create-book-entry">
      <form:form modelAttribute="bookEntryForm" action="/books/createEntry/${book.id}" method="POST">
        <form:input type="hidden" path="bookId" value="${book.id}"/>
        <div class="form-row">
          <div class="form-group col-md-4">
            <label for="inputInventoryNumber">
              <spring:message code="books.detail.entryform.inventoryNumber"/>
            </label>
            <form:input type="text" path="inventoryNumber" class="form-control" id="inputInventoryNumber"/>
            <form:errors path="inventoryNumber" cssClass="error"/>
          </div>
          <div class="form-group col-md-4">
            <label for="inputSignature">
              <spring:message code="books.detail.entryform.signature"/>
            </label>
            <form:input type="text" path="signature" class="form-control" id="inputSignature"/>
            <form:errors path="signature" cssClass="error"/>
          </div>
          <div class="form-group col-md-4">
            <label for="inputPhysicalState">
              <spring:message code="books.detail.entryform.physicalState"/>
            </label>
            <form:input type="text" path="physicalState" class="form-control" id="inputPhysicalState"/>
            <form:errors path="physicalState" cssClass="error"/>
          </div>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">
          <spring:message code="books.detail.entryform.submit"/>
        </button>
      </form:form>
    </div>
  </sec:authorize>


  <div class="book-entries-list col-md-6">
    <h2><spring:message code="books.detail.entries.title"/></h2>
    <c:forEach items="${book.entries}" var="entry">
      <t:bookEntry entry="${entry}"/>
    </c:forEach>
  <div>

  <!-- Modal -->
  <div class="modal fade" id="bookEntryEditModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel"><spring:message code="books.detail.entries.edit.title"/></h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <form:form modelAttribute="editBookEntryForm" action="/books/detail/${book.id}/editEntry" method="POST">
            <form:input type="hidden" id="entryId" path="id"/>
            <div class="form-row">
              <div class="form-group col-md-4">
                <label for="editInventoryNumber">
                  <spring:message code="books.detail.entryform.inventoryNumber"/>
                </label>
                <form:input type="text" path="inventoryNumber" class="form-control" id="editInventoryNumber"/>
                <form:errors path="inventoryNumber" cssClass="error"/>
              </div>
              <div class="form-group col-md-4">
                <label for="editSignature">
                  <spring:message code="books.detail.entryform.signature"/>
                </label>
                <form:input type="text" path="signature" class="form-control" id="editSignature"/>
                <form:errors path="signature" cssClass="error"/>
              </div>
              <div class="form-group col-md-4">
                <label for="editPhysicalState">
                  <spring:message code="books.detail.entryform.physicalState"/>
                </label>
                <form:input type="text" path="physicalState" class="form-control" id="editPhysicalState"/>
                <form:errors path="physicalState" cssClass="error"/>
              </div>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit">
              <spring:message code="books.detail.entries.edit.submit"/>
            </button>
          </form:form>
        </div>
      </div>
    </div>
  </div>

  <c:if test="${editEntryFormErrors}">
    <div id="editEntryFormErrors" class="display-none"/>
  </c:if>


  <div class="modal fade" id="bookEntryRemoveModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel"><spring:message code="books.detail.entries.delete.title"/></h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <spring:message code="books.detail.entries.delete.body"/>
          <form id="removeBookEntryForm" action="/books/detail/${book.id}/removeEntry" method="POST">
            <input type="hidden" id="removeEntryId" name="removeEntryId"/>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">
            <spring:message code="books.detail.entries.delete.cancel"/>
          </button>
          <button id="confirmRemoveBookEntry" type="button" class="btn btn-primary">
            <spring:message code="books.detail.entries.delete.confirm"/>
          </button>
        </div>
      </div>
    </div>
  </div>
</t:page>
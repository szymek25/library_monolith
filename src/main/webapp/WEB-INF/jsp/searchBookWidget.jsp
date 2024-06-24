<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:choose>
  <c:when test="${connectionError}">
    <spring:message code="books.add.searchwidget.result.connectionError"/>
  </c:when>
  <c:when test="${empty books}">
    <spring:message code="books.add.searchwidget.result.empty"/>
  </c:when>
  <c:otherwise>
    <div class="container-fluid">
      <c:forEach var="book" items="${books}">
        <div class="row">
          <div class="col-md-1">
            <input type="radio" name="selectedBook"
            data-isbn="${book.isbnIssn}"
            data-title="${book.title}"
            data-author="${book.author}"
            data-publisher="${book.publisher}"
            data-publicationYear="${book.publicationYear}">
          </div>
          <div class="book-result col-md-10">
            <p> <spring:message code="books.add.searchwidget.result.isbn"/> : ${book.isbnIssn} </p>
            <p> <spring:message code="books.add.searchwidget.result.title"/> : ${book.title} </p>
            <p> <spring:message code="books.add.searchwidget.result.author"/> : ${book.author} </p>
            <p> <spring:message code="books.add.searchwidget.result.publisher"/> : ${book.publisher} </p>
            <p> <spring:message code="books.add.searchwidget.result.publicationYear"/> : ${book.publicationYear} </p>
          </div>
        </div>
      </c:forEach>
    </div>
    <button id="confirmBookSelection" class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="books.add.searchwidget.select"/>
    </button>
  </c:otherwise>
</c:choose>


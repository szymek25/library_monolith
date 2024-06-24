<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page title="books.search.title">

  <div class="book-info col-md-6">
    <c:set var="book" value="${entry.book}"/>
    <p><spring:message code="books.search.book.isbn"/>: ${book.isbn} </p>
    <p><spring:message code="books.search.book.title"/>: ${book.title} </p>
    <p><spring:message code="books.search.book.author"/>: ${book.author} </p>
    <p><spring:message code="books.search.book.publicationYear"/>: ${book.publicationYear} </p>
    <p><spring:message code="books.search.book.publisher"/>: ${book.publisher} </p>
  </div>

  <div class="book-entries-list col-md-6">
    <h2><spring:message code="books.search.entry"/></h2>
    <t:bookEntry entry="${entry}"/>
  </div>
</t:page>
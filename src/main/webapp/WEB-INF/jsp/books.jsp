<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:page title="books.list.title">

  <sec:authorize access="hasRole('EMPLOYEE')">
    <div class="row">
      <div class="col find-by-barcode-box">
        <p> <spring:message code="books.list.findBookEntry.info"/> </p>
        <form action="/books/findBookEntry" method="POST">
          <input class="barcode-input-holder" name="barcode" type="text" class="form-control col-md-4" placeholder="<spring:message code="books.list.findBookEntry.input.placeholder"/>"/>
        </form>
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
            <spring:message code="books.list.table.title"/>
          </th>
          <th scope="col">
            <spring:message code="books.list.table.author"/>
          </th>
          <th scope="col">
            <spring:message code="books.list.table.publicationYear"/>
          </th>
          <th scope="col">
            <spring:message code="books.list.table.category"/>
          </th>
          <sec:authorize access="hasRole('EMPLOYEE')">
            <th scope="col">
              <spring:message code="books.list.table.actions"/>
            </th>
          </sec:authorize>
        </tr>
        </thead>
        <c:forEach items="${books}" var="book">
          <tr>
            <th scope="row">${book.id}</th>
            <td><a href="/books/detail/${book.id}">${book.title}</td></a>
            <td>${book.author}</td>
            <td>${book.publicationYear}</td>
            <td>${book.category.name}</td>
            <sec:authorize access="hasRole('EMPLOYEE')">
              <td>
                <a class="btn btn-primary btn-block" href="edit/${book.id}">
                  <spring:message code="books.list.table.actions.edit"/>
                </a>
                <c:if test="${empty book.entries}">
                  <form action="delete/${book.id}" method="POST">
                    <button class="btn btn-primary btn-block" type="submit">
                      <spring:message code="books.list.table.actions.delete"/>
                    </button>
                  </form>
                </c:if>
              </td>
            </sec:authorize>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <t:paginator pages="${pages}" current="${currentPage}"/>
    </div>
  </div>
</t:page>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="books.add.title">

  <form:form modelAttribute="addBookForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="books.add.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputIsbn">
          <spring:message code="books.add.form.isbn"/>
        </label>
        <form:input type="text" path="isbn" class="form-control" id="inputIsbn"/>
        <form:errors path="isbn" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputCategory">
          <spring:message code="books.add.form.category"/>
        </label>
        <form:input type="hidden" path="categoryId" class="form-control" id="inputCategory"/>
        <select id="category-selector" class="custom-select">
          <c:forEach items="${categories}" var="category">
            <option class="role-option" value="${category.id}" ${category.id == selectedCategory ?
            'selected' :''}>${category.name}</option>
          </c:forEach>
        </select>
        <form:errors path="categoryId" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputAuthor">
          <spring:message code="books.add.form.author"/>
        </label>
        <form:input type="text" path="author" class="form-control" id="inputAuthor"/>
        <form:errors path="author" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputpublicationYear">
          <spring:message code="books.add.form.publicationYear"/>
        </label>
        <form:input type="text" path="publicationYear" class="form-control"
                    id="inputpublicationYear"/>
        <form:errors path="publicationYear" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputPublisher">
          <spring:message code="books.add.form.publisher"/>
        </label>
        <form:input type="text" path="publisher" class="form-control" id="inputPublisher"/>
        <form:errors path="publisher" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputTitle">
          <spring:message code="books.add.form.booktitle"/>
        </label>
        <form:input type="text" path="title" class="form-control" id="inputTitle"/>
        <form:errors path="title" cssClass="error"/>
      </div>
    </div>


    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="books.add.form.submit"/>
    </button>
  </form:form>

  <div class="find-by-isbn-box">
    <spring:message code="books.add.searchwidget.input.info"/>
    <input id="findByIsbnInput" type="text" class="form-control col-md-6" placeholder="<spring:message code="books.add.searchwidget.input.placeholder"/>"/>
  </div>

<div class="modal fade" id="searchBookWidget" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel"><spring:message code="books.add.searchwidget.title"/></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div id="searchBookWidgetBody" class="modal-body">
        <%-- Body will be inserted by jquery after succes ajax call --%>
      </div>
    </div>
  </div>
</div>
</t:page>
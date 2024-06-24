<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page title="books.edit.title">

  <form:form modelAttribute="editBookForm" method="POST">
    <div class="form-row">
      <h1>
        <spring:message code="books.edit.form.title"/>
      </h1>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputIsbn">
          <spring:message code="books.edit.form.isbn"/>
        </label>
        <form:input type="text" path="isbn" class="form-control" id="inputIsbn"/>
        <form:errors path="isbn" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputCategory">
          <spring:message code="books.edit.form.category"/>
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
          <spring:message code="books.edit.form.author"/>
        </label>
        <form:input type="text" path="author" class="form-control" id="inputAuthor"/>
        <form:errors path="author" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputPublicationYear">
          <spring:message code="books.edit.form.publicationYear"/>
        </label>
        <form:input type="text" path="publicationYear" class="form-control"
                    id="inputPublicationYear"/>
        <form:errors path="publicationYear" cssClass="error"/>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="inputPublisher">
          <spring:message code="books.edit.form.publisher"/>
        </label>
        <form:input type="text" path="publisher" class="form-control" id="inputPublisher"/>
        <form:errors path="publisher" cssClass="error"/>
      </div>
      <div class="form-group col-md-6">
        <label for="inputTitle">
          <spring:message code="books.edit.form.booktitle"/>
        </label>
        <form:input type="text" path="title" class="form-control" id="inputTitle"/>
        <form:errors path="title" cssClass="error"/>
      </div>
    </div>

    <button class="btn btn-lg btn-primary btn-block" type="submit">
      <spring:message code="books.edit.form.submit"/>
    </button>
  </form:form>

</t:page>
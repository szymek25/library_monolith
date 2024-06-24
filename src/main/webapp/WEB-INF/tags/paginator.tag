<%@ attribute name="pages" required="true" type="java.lang.Integer" %>
<%@ attribute name="current" required="true" type="java.lang.Integer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:if test="${pages > 1}">
  <c:url value="" var="url"/>
  <nav aria-label="...">
    <ul class="pagination">
      <c:forEach begin="0" end="${pages - 1}" var="step">
        <li class="page-item ${current eq step ? 'active' : ''}">
          <a class="page-link" href="${url}?page=${step}<c:if test="${not empty urlParamsToPass}">&${urlParamsToPass}</c:if>">${step+ 1} </a>
        </li>
      </c:forEach>
    </ul>
  </nav>
</c:if>
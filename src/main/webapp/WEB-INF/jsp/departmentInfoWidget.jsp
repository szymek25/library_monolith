<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<p>
  <b><spring:message code="books.detail.entries.departmentmodal.departmentName"/>: </b>
  ${department.name}
</p>
<p>
  <b><spring:message code="books.detail.entries.departmentmodal.addressLine1"/>: </b>
  ${department.addressLine1}
</p>
<p>
  <b><spring:message code="books.detail.entries.departmentmodal.town"/>: </b>
  ${department.town}
</p>
<p>
  <b><spring:message code="books.detail.entries.departmentmodal.postalCode"/>: </b>
  ${department.postalCode}
</p>

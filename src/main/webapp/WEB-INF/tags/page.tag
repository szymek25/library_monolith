<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <title>
      <spring:message code="${title}"/>
    </title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
    <link href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"
          rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
  </head>
  <body>
  <t:navigation/>
  <main role="main" class="container">

    <div class="content">
      <c:if test="${not empty errorMessageHolder}">
        <div class="messages">
          <c:forEach items="${errorMessageHolder}" var="errorMessage">
            <div class="alert alert-danger" role="alert">
              <spring:message code="${errorMessage}"/>
            </div>
          </c:forEach>
        </div>
      </c:if>
      <c:if test="${not empty confMessageHolder}">
        <div class="messages">
          <c:forEach items="${confMessageHolder}" var="confMessage">
            <div class="alert alert-success" role="alert">
              <spring:message code="${confMessage}"/>
            </div>
          </c:forEach>
        </div>
      </c:if>
      <jsp:doBody/>
    </div>
  </main>


  <script src="/webjars/jquery/1.9.1/jquery.min.js"></script>
  <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="/js/main.js"></script>
  </body>
</html>
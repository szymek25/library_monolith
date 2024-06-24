<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<sec:authorize access="isAuthenticated()">
  <header>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">

      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <sec:authorize access="hasRole('MANAGER')">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="users" data-toggle="dropdown"
                 aria-haspopup="true"
                 aria-expanded="false">
                <spring:message code="navbar.users.title"/>
              </a>
              <div class="dropdown-menu" aria-labelledby="users">
                <a class="dropdown-item" href="/users/list">
                  <spring:message code="navbar.users.list"/>
                </a>
                <a class="dropdown-item" href="/users/new">
                  <spring:message code="navbar.users.add"/>
                </a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="users" data-toggle="dropdown"
                 aria-haspopup="true"
                 aria-expanded="false">
                <spring:message code="navbar.depertments.title"/>
              </a>
              <div class="dropdown-menu" aria-labelledby="users">
                <a class="dropdown-item" href="/departments/list">
                  <spring:message code="navbar.depertments.list"/>
                </a>
                <a class="dropdown-item" href="/departments/new">
                  <spring:message code="navbar.departments.add"/>
                </a>
              </div>
            </li>
          </ul>
        </sec:authorize>
        <sec:authorize access="hasRole('EMPLOYEE')">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="users" data-toggle="dropdown"
                 aria-haspopup="true"
                 aria-expanded="false">
                <spring:message code="navbar.users.title"/>
              </a>
              <div class="dropdown-menu" aria-labelledby="users">
                <a class="dropdown-item" href="/users/list">
                  <spring:message code="navbar.users.list"/>
                </a>
                <a class="dropdown-item" href="/users/newCustomer">
                  <spring:message code="navbar.users.add"/>
                </a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="books" data-toggle="dropdown"
                 aria-haspopup="true"
                 aria-expanded="false">
                <spring:message code="navbar.books.title"/>
              </a>
              <div class="dropdown-menu" aria-labelledby="books">
                <a class="dropdown-item" href="/books/new">
                  <spring:message code="navbar.books.books.add"/>
                </a>
                <a class="dropdown-item" href="/books/list">
                  <spring:message code="navbar.books.books.list"/>
                </a>
                <a class="dropdown-item" href="/categories/list">
                  <spring:message code="navbar.books.categories.list"/>
                </a>
                <a class="dropdown-item" href="/categories/new">
                  <spring:message code="navbar.books.categories.add"/>
                </a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/rents/list">
                <spring:message code="navbar.rents.list"/>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/reservations/list">
                <spring:message code="navbar.reservations.list"/>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/orders/list">
                <spring:message code="navbar.orders.list"/>
                <span class="badge badge-light">${newOrders}</span>
              </a>
            </li>
          </ul>
        </sec:authorize>
        <sec:authorize access="hasRole('USER')">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
              <a class="nav-link" href="/books/list">
                <spring:message code="navbar.books.books.list"/>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/rents/list/user">
                <spring:message code="navbar.rents.list"/>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/reservations/list/user">
                <spring:message code="navbar.reservations.list.user"/>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="/orders/list/user">
                <spring:message code="navbar.orders.list.user"/>
              </a>
            </li>
          </ul>
        </sec:authorize>

        <form class="form-inline my-2 my-lg-0" action="/perform_logout" method="post">
          <button class="btn btn-secondary my-2 my-sm-0" type="submit">Wyloguj</button>
        </form>
      </div>
    </nav>
  </header>
</sec:authorize>
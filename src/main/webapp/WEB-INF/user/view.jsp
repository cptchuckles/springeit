<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
    <title>SpringeIt - <c:out value="${user.username}" />'s Profile</title>
  </head>
  <body>
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5" id="top">
      <div class="d-flex flex-row justify-content-between mb-5">
        <div>
          <h1><c:out value="${user.username}" /></h1>
          <c:if test="${user.isAdmin()}">
          <p>This user is an administrator.</p>
          </c:if>
          <c:if test="${user.id eq currentUser.id || currentUser.isAdmin()}">
          <p><a href="/users/${user.id}/edit">Edit Profile</a></p>
          </c:if>
        </div>
        <div>
          <p>Email address: <c:out value="${user.email}" /></p>
          <p>Member since <fmt:formatDate value="${user.createdAt}" pattern="d MMM, yyyy" /></p>
        </div>
      </div>

      <div class="d-flex flex-row justify-content-between">
        <h3>Total Cringe: <c:out value="${user.getTotalCringe()}" /></h3>
        <div>
          <a href="/cringe" class="btn btn-secondary">&lt; Back to CringeFeed</a>
          <a href="/cringe/new" class="btn btn-success">+ Post Cringe</a>
        </div>
      </div>

      <c:forEach var="oneCringe" items="${user.cringe}">
      <%@ include file="../cringe/_insert.jsp" %>
      </c:forEach>
    </div>
  </body>
</html>

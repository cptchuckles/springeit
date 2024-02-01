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
    <title>SpringeIt</title>
  </head>
  <body>
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5">
      <div class="d-flex flex-row justify-content-between align-items-baseline">
        <h1>Cringe feed</h1>
        <a href="/cringe/new" class="btn btn-primary">Post Cringe!</a>
      </div>
      <c:choose>
      <c:when test="${allCringe.size() gt 0}">
      <c:forEach var="oneCringe" items="${allCringe}">
      <%@ include file="./_insert.jsp" %>
      </c:forEach>
      </c:when>
      <c:otherwise>
      <div class="card bg-secondary text-center text-light p-2">
        <h2>Nobody has posted any cringe</h2>
        <a href="/cringe/new" class="link-light">Are you going to ruin that?</a>
      </div>
      </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>

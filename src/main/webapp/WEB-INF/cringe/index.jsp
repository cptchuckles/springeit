<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <h1>All Cringe</h1>
        <a href="/cringe/new" class="btn btn-primary">Post Cringe!</a>
      </div>
      <c:forEach var="oneCringe" items="${allCringe}">
      <%@ include file="./_insert.jsp" %>
      </c:forEach>
    </div>
  </body>
</html>

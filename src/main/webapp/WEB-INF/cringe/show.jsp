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
    <title>SpringeIt - Post Cringe</title>
  </head>
  <body>
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5" id="top">
      <div class="d-flex flex-row justify-content-between">
        <div>
          <h1 class="col"><c:out value="${cringe.headline}" /></h1>
          <small class="lh-sm text-muted">
            Posted by <c:out value="${cringe.user.username}" />
            on <fmt:formatDate value="${cringe.createdAt}" pattern="d MMM, yyyy" />
            <c:if test="${cringe.user.id eq currentUser.id}">
            <a class="mx-2 px-2 border-start border-dark" href="/cringe/${cringe.id}/edit">Edit</a>
            </c:if>
          </small>
        </div>
        <h1 class="bg-dark text-light p-3 text-center d-flex flex-column justify-content-center rounded-pill">
          <c:out value="${cringe.totalRating()}" />
        </h1>
      </div>

      <h3 class="border border-5 bg-body-tertiary my-3 text-center py-3 rounded">
        <a href="${cringe.url}" target="_blank"><c:out value="${cringe.url}" /></a>
      </h3>

      <p class="fs-3" style="whitespace: pre">
        <c:out value="${cringe.description}" />
      </p>

      <div class="text-center my-2 py-2 border-top border-bottom border-secondary" width="80%">
        <a href="#" class="btn btn-success btn-lg">+ CRINGE</a>
        <span class="text-muted">← RATE THIS CRINGE →</span>
        <a href="#" class="btn btn-danger btn-lg">- BORING</a>
      </div>

      <!-- comments go here lmao -->

      <div class="d-flex flex-row w-100 mt-5 py-5 border-top border-secondary align-items-baseline">
        <div class="col">
          <a href="/cringe" class="btn btn-secondary">Return to Cringe Feed</a>
        </div>
        <div class="col text-center">
          <a href="#top" class="btn btn-secondary">Scroll to Top</a>
        </div>
        <div class="col text-end">
          <a href="/cringe/${cringe.id}/whine">Report this Cringe</a>
        </div>
      </div>
    </div>
  </body>
</html>

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
    <link rel="stylesheet" href="/css/cringe.css" />
    <title>SpringeIt - Post Cringe</title>
    <%@ include file="../lib/importmap.jsp" %>
    <script type="module" src="/js/CommentForm.js"></script>
    <script type="module" src="/js/CommentTree.js"></script>
    <script type="module" src="/js/CringeComment.js"></script>
  </head>
  <body>
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5" id="top">
      <div class="d-flex flex-row justify-content-between">
        <div>
          <h1 class="col"><c:out value="${cringe.headline}" /></h1>
          <small class="lh-sm text-muted">
            Posted by <a href="/users/${cringe.user.id}"><c:out value="${cringe.user.username}" /></a>
            on <fmt:formatDate value="${cringe.createdAt}" pattern="d MMM, yyyy" />
            <c:if test="${cringe.user.id eq currentUser.id || currentUser.isAdmin()}">
            <a class="mx-2 px-2 border-start border-dark" href="/cringe/${cringe.id}/edit">Edit</a>
            </c:if>
          </small>
        </div>
        <h1 style="min-width: 3ch" class="bg-dark text-light p-3 text-center d-flex flex-column justify-content-center rounded-pill">
          <c:out value="${cringe.getTotalRating()}" />
        </h1>
      </div>

      <h3 class="border border-5 bg-body-tertiary my-3 text-center py-3 rounded">
        <a href="${cringe.url}" target="_blank"><c:out value="${cringe.url}" /></a>
      </h3>

      <p class="fs-3 lh-sm" style="word-wrap: break-word; white-space: pre-line"><c:out value="${cringe.description}" />
      </p>

      <div class="text-center my-2 py-2 border-top border-bottom border-secondary" width="80%">
        <form id="rate-up" action="/cringe/${cringe.id}/rate" method="POST">
          <input type="hidden" name="delta" value="1" />
        </form>
        <form id="rate-down" action="/cringe/${cringe.id}/rate" method="POST">
          <input type="hidden" name="delta" value="-1" />
        </form>
        <c:choose>
        <c:when test="${rating eq 'up'}">
        <input type="submit" form="rate-up" value="+ CRINGE" class="btn btn-success btn-lg" />
        <span class="text-muted">← RATE THIS CRINGE →</span>
        <input type="submit" form="rate-down" value="- BORING" class="btn btn-light btn-lg text-danger border border-3 border-danger" />
        </c:when>
        <c:when test="${rating eq 'down'}">
        <input type="submit" form="rate-up" value="+ CRINGE" class="btn btn-light btn-lg text-success border border-3 border-success" />
        <span class="text-muted">← RATE THIS CRINGE →</span>
        <input type="submit" form="rate-down" value="- BORING" class="btn btn-danger btn-lg" />
        </c:when>
        <c:otherwise>
        <input type="submit" form="rate-up" value="+ CRINGE" class="btn btn-light btn-lg text-success border border-3 border-success" />
        <span class="text-muted">← RATE THIS CRINGE →</span>
        <input type="submit" form="rate-down" value="- BORING" class="btn btn-light btn-lg text-danger border border-3 border-danger" />
        </c:otherwise>
        </c:choose>
      </div>

      <div class="d-flex flex-row w-100 py-5 align-items-baseline">
        <div class="col">
          <a href="/cringe" class="btn btn-secondary">Return to Cringe Feed</a>
        </div>
        <div class="col text-end">
          <a href="/cringe/${cringe.id}/whine">Report this Cringe</a>
        </div>
      </div>

      <!-- comments go here lmao -->
      <comment-tree cringe-id="${cringe.id}" current-user-id="${currentUser.id}"></comment-tree>

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

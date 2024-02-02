<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<style>
button#whine {
  display: flex;
  flex-direction: row;
  align-items: center;
  background-color: firebrick;
  box-shadow: 4px 4px black;
  width: fit-content;
  margin: auto;
}
</style>
  </head>
  <body style="background-color: #f78; text-align: center">
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5" id="top">
      <h4>File a report:</h4>
      <h1 class="bg-white p-5 rounded-5"><c:out value="${cringe.headline}" /></h1>
      <form action="/cringe/${cringe.id}/whine" method="POST" class="form my-5">
        <div class="form-check d-flex flex-row align-items-center gap-2" style="font-size: 1.2em; width: fit-content; margin: auto; margin-bottom: 2em;">
          <input required type="checkbox" name="iAmABaby" id="iAmABaby" class="form-check-input my-3" />
          <label class="form-check-label" for="iAmABaby">I am a little baby</label>
        </div>
        <button id="whine" class="btn btn-danger">
          <img src="/img/baby-girl-cry-svgrepo-com.svg" width="120px" style="filter: invert(.8)" alt="you" />
          <h2>Whine</h2>
        </button>
      </form>
      <a href="/cringe/${cringe.id}" class="link link-dark">do not whine</a>
    </div>
  </body>
</html>

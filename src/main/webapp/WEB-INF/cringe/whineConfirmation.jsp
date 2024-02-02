<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
    <title>SpringeIt - Thanks for Whining</title>
  </head>
  <body style="background-color: #f78; text-align: center">
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5" style="text-align: center" id="top">
      <img src="/img/baby-girl-cry-svgrepo-com.svg" width="50%" alt="literally you" />
      <h1>Your whine has been recorded.</h1>
      <p><a href="/cringe" class="link link-dark">Return to CringeFeed</a></p>
    </div>
  </body>
</html>

<%@ page isErrorPage="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
    <title>SpringeIt - Edit Cringe</title>
  </head>
  <body>
    <%@ include file="../insert/header.jsp" %>
    <div class="container mt-5">
      <form:form id="edit" class="form" action="/cringe/${cringe.id}" method="PUT" modelAttribute="cringe">
        <p>
          <form:errors class="form-text text-danger" path="headline" />
          <form:input class="form-control fs-2" path="headline" placeholder="Enter a Clickbait Headline" />
        </p>
        <p class="d-flex flex-row gap-2 align-items-baseline">
          <form:label class="form-label" path="url">Source of cringe URL:</form:label>
          <form:errors class="form-text text-danger" path="url" />
          <form:input class="form-control col" path="url" />
        </p>
        <p>
          <form:label class="form-label" path="description">Your commentary or description of the cringe:</form:label>
          <form:errors class="form-text text-danger" path="description" />
          <form:textarea class="form-control" path="description"></form:textarea>
        </p>
      </form:form>
      <form:form id="delete" action="/cringe/${cringe.id}" method="DELETE"></form:form>
      <div class="d-flex flex-row justify-content-between">
        <a href="/cringe" class="btn btn-secondary">Cancel</a>
        <input type="submit" form="delete" value="Delete" class="btn btn-danger" />
        <input type="submit" form="edit" value="Update Cringe" class="btn btn-success" />
      </div>
    </div>
  </body>
</html>

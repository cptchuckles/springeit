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
    <title>SpringeIt</title>
  </head>
  <body>
    <h1 class="text-center m-5 border-bottom border-secondary p-3">SpringeIt</h1>
    <div class="container mt-5">
      <div class="d-flex flex-row gap-3">
        <div class="card col p-3">
          <h3>Register</h3>
          <form:form class="form" action="/register" method="POST" modelAttribute="userRegister">
            <p>
              <form:errors class="form-text text-danger" path="username" />
              <form:input class="form-control" path="username" placeholder="Choose a Username" />
            </p>
            <p>
              <form:errors class="form-text text-danger" path="email" />
              <form:input type="email" class="form-control" path="email" placeholder="Enter your Email" />
            </p>
            <p>
              <form:errors class="form-text text-danger" path="password" />
              <form:input type="password" class="form-control" path="password" placeholder="Enter a Password" />
            </p>
            <p>
              <form:errors class="form-text text-danger" path="confirmPassword" />
              <form:input type="password" class="form-control" path="confirmPassword" placeholder="Confirm your Password" />
            </p>
            <input type="submit" value="Create an Account" class="btn btn-success" />
          </form:form>
        </div>
        <div class="card col p-3">
          <h3>Login</h3>
          <form:form class="form" action="/login" method="POST" modelAttribute="userLogin">
            <p>
              <form:errors class="form-text text-danger" path="email" />
              <form:input type="email" class="form-control" path="email" placeholder="Enter your Email" />
            </p>
            <p>
              <form:errors class="form-text text-danger" path="password" />
              <form:input type="password" class="form-control" path="password" placeholder="Enter your Password" />
            </p>
            <input type="submit" value="Sign In" class="btn btn-primary" />
          </form:form>
        </div>
      </div>
    </div>
  </body>
</html>

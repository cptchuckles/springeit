<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
        <jsp:include page="lib/importmap.jsp" />
        <title>CringeIt Spring</title>
    </head>
    <body>
        <script type="module" src="/js/ClickCounter.js"></script>
        <div class="container mt-5">
            <h1>yeet</h1>
            <click-counter></click-counter>
        </div>
    </body>
</html>

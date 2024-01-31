<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
        <title>CringeIt Spring</title>
        <script type="importmap">
            {
                "imports": {
                "preact": "https://esm.sh/preact@10.19.2",
                "https://esm.sh/preact/hooks": "https://esm.sh/preact@10.19.2/hooks",
                "https://esm.sh/preact-custom-element": "https://esm.sh/preact-custom-element@4.3.0?external=preact",
                "https://esm.sh/htm/preact": "https://esm.sh/htm@3.1.1/preact?external=preact"
                }
            }
        </script>
    </head>
    <body>
        <div class="container mt-5">
            <h1>yeet</h1>
            <click-counter></click-counter>
        </div>
        <script type="module" src="/js/Counter.js"></script>
    </body>
</html>

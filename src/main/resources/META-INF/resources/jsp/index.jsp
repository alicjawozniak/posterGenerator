<%--
  Created by IntelliJ IDEA.
  User: alicja
  Date: 23.10.16
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <%--<c:if test="${text2!=null && !text2.equals('')}">--%>
    <%--Zalogowany jako: ${text2}<br/>--%>
    <%--</c:if>--%>
    <form action="http://localhost:8080/generate" method="POST">
        <label>Podaj tekst</label>
        <input type="text" name="text" required autofocus/>
        <button type="submit">Generate</button>
    </form>
</div>
</body>
</html>

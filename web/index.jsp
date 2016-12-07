<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv='Refresh' content='0;URL=<%=request.getContextPath()%>/common.home.action' />
    <title>Redirecionando</title>
</head>
<body>
    Você está sendo redirecionado!<br />
    Caso o redirecionamento não ocorra
    <a href="<%=request.getContextPath()%>/common.home.action">clique aqui</a>.
</body>
</html>

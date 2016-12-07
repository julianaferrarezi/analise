<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib uri="http://displaytag.sf.net" prefix="display" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://www.fc.unesp.br/skin" prefix="skin" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta name="header" content="Sistema em manutenção">
  <title>Sistema em manutenção</title>
  <mtw:inputMaskConfig />
</head>

<c:set var="linkRetornar"><%=request.getContextPath()%>/common.logout.action</c:set>

<script type="text/javascript">

function retornar(){
    $('#form').attr('action', '${linkRetornar}');
    $('#form').submit();
}

</script>

<style type="text/css">
	.azul {
		color: #003399;	
	}
</style>

<body>
	<form name="form" id="form" method="post">
		<center>
			<h1>
			<span class="azul">Sistema de Gestão Acadêmica em manutenção</span><br/><br/>
			Motivo: <span class="azul">Implantação do sistema de pós-graduação</span><br/>
			Previsão de volta: <span class="azul">Segunda-feira, 07 de dezembro de 2015 às 09:00.</span><br/><br/>
						
			<img border="0" src="<%=request.getContextPath()%>/imgs/manutencao.png" width="200" height="200" /><br/><br/>
			
			Agradecemos a compreensão!
			</h1>
			
			<br/>
			
			<input type="button" class="botao" value="Retornar ao portal" onclick="javascript:retornar();"/>

		</center>
	</form>	
</body>
</html>
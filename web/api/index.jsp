<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>API de Integração RESTful</title>
<LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style">
</head>
<body>
	<h1>API de Integração RESTful</h1>
	<h2>Utilização</h2>
	<h4>Para chamar a API use o padrão abaixo:</h4>
	https://{endereço do servidor}/{contexto}/api/{versao}/{nome da coleção}/{parâmetros}
	<h4>Exemplo</h4>
	Buscar aluno de graduação de id 100<br/>
	https://sistemas.unesp.br/academico/api/v1/alunosGraduacao/100<br/><br/>
	
	Listar estruturas curriculares do aluno de graduação de id 100<br/>
	https://sistemas.unesp.br/academico/api/v1/alunosGraduacao/100/estruturasCurriculares<br/><br/>
	
	<h4>Retorno</h4>
	O retorno padrão será no formato JSON, caso deseje XML adicione .xml ao final da URL. Exemplo:<br/><br/>
	https://sistemas.unesp.br/academico/api/v1/alunosGraduacao/100.xml<br/><br/>
	
	<h2>Documentação</h2>
	Para consultar a documentação dos recursos disponí­veis <a href="docs/" target="_blank">clique aqui</a><br/><br/>
	
	<h2>Cliente padrão</h2>
	<a href="jars/graduacao-api-client.jar">Download do cliente padrão</a><br/>
	<a href="jars/jersey-bundle-1.14.jar">Download da biblioteca do Jersey</a><br/>
	<a target="_blank" href="docs-client/">Javadoc</a>
	
</body>
</html>
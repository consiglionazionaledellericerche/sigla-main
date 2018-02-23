<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*,it.cnr.jada.UserContext"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Cambio password</title>
<body class="Workspace">

<% LoginBP bp = (LoginBP)BusinessProcess.getBusinessProcess(request);
	UserContext uc = HttpActionContext.getUserContext(session);
	 bp.openForm(pageContext,"Login",null); %>

	<table align="center" class="Window">
	<tr><td>
	 <table class="Panel" cellspacing=20>
	  <tr>
		<td rowspan="2" valign="middle"><img src="img/question.gif"></td>
	  	<td style="font-size : 14px">Per cambiare la password cliccare sul seguente collegamento :</td></tr>
	  <tr>
	  	<td style="font-size : 14px" align=center><% out.println("<a href='"+bp.getLdapLinkCambioPassword(uc)+"'>Pagina di cambio password</a>"); %>
	  </tr>
  </table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
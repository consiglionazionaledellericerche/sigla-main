<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>
<html>
<head>
<% it.cnr.jada.util.jsp.JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" archive="scripts.jar" src="scripts/util.js"></script>
<title>Logo</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body class="Workspace">
<script>
restoreWorkspace();
</script>

<form name="mainForm" action="FormAction.do">
<% BusinessProcess bp = BusinessProcess.getBusinessProcess(request);
	BusinessProcess.encode(bp,pageContext); %>
<input type="hidden" name="comando" value="doDefault">
<table class="Window" cellspacing="0" cellpadding="2" align="center" width="280">
		<tr><td class="FormTitle">Informazione</td></tr>
	<tr>
		<td align="center">
			<table class="Panel" width="100%">
			  <tr>
					<td valign="center"><img src="img/error.gif"></td>
				  	<td valign="center" width="100%">
				  		Pagina scaduta.
				  		<br>E' stata richiesta una operazione da una pagina non più valida.
<% if (bp != null) { %>
						<br>Per ritornare alla pagina corrente premere il bottone 'chiudi'.
<% } %>
					</td>
			  </tr>
			</table>
			<hr>
<% if (bp != null) { %>
			<button onclick="if (disableDblClick()) submitForm('doDefault')">Chiudi</button>
<% } %>
		</td>
	</tr>
</table>
</form>
</body>
</html>
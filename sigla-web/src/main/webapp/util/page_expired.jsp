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
<table class="Window card" cellspacing="0" cellpadding="2" align="center" width="280">
		<tr><td class="FormTitle card-header h5">Informazione</td></tr>
	<tr>
		<td align="center">
			<table class="Panel" width="100%">
			  <tr>
					<td valign="center">
						<%if (bp.getParentRoot().isBootstrap()) { %>
		  					<i class="fa fa-exclamation-circle fa-fw fa-2x text-danger" aria-hidden="true"></i>
		  				<%} else {%>
		  					<img src="img/error.gif">
		  				<%}%>
					</td>
				  	<td valign="center h5" width="100%">
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
			<button class="btn btn-primary" onclick="if (disableDblClick()) submitForm('doDefault')">Chiudi</button>
<% } %>
		</td>
	</tr>
</table>
</form>
</body>
</html>
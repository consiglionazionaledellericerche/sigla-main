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

<form name="mainForm" action="FormAction.do" action-ng="FormAction.do">
<% BusinessProcess bp = BusinessProcess.getBusinessProcess(request);
	BusinessProcess.encode(bp,pageContext); %>
<input type="hidden" name="comando" value="doDefault">
<%if (!bp.getParentRoot().isBootstrap()) { %>
<table class="Window card col-5" cellspacing="0" cellpadding="2" align="center" width="280">
	<tbody>
		<tr>
			<td class="FormTitle card-header h5">Informazione</td>
		</tr>
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
			</td>
		</tr>
		<tr>
			<td align=center>		
				<hr>
				<table class="Panel" width="100%">
					<tr>
						<% if (bp != null) { %>
							<td>
								<button class="btn btn-primary btn-block" onclick="if (disableDblClick()) submitForm('doDefault')">Chiudi</button>
							</td>
						<% } %>
					</tr>
				</table>
			</td>
		</tr>
	</tbody>
</table>
<% } else { %>	
	<div class="col-md-6 offset-md-3 mx-auto">
		<div class="card">
		  <h3 class="card-header h2 text-danger"><i class="fa fa-exclamation-circle fa-fw fa-2x text-danger" aria-hidden="true"></i> Errore</h3>
		  <div class="card-block p-3">
		    <p class="card-title">Pagina scaduta.
					  		<br>E' stata richiesta una operazione da una pagina non più valida.
							<% if (bp != null) { %>
							<br>Per ritornare alla pagina corrente premere il bottone 'chiudi'.
							<% } %></p>
		    <div class="col-sm-offset-5 text-center">
				<button class="btn btn-primary btn-block" onclick="if (disableDblClick()) submitForm('doDefault')">Chiudi</button>
			</div>
		  </div>
		</div>
	</div>
<% }%>	
</form>
</body>
</html>
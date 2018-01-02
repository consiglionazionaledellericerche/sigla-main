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
<table class="Window" cellspacing="0" cellpadding="2" align="center" width="280">
		<tr><td class="FormTitle">Informazione</td></tr>
	<tr>
		<td align="center">
			<table class="Panel" width="100%">
			  <tr>
					<td valign="center"><img src="img/error.gif"></td>
				  	<td valign="center" width="100%">
				  		Operazione in corso.
				  		<br>L'ultima operazione richiesta è ancora in corso.
						<br>Per controllare se l'operazione è terminata premere il bottone 'riprova'.
<% if (bp != null) { %>
						<br>Per chiudere la pagina premere il bottone 'chiudi' (l'operazione NON verrà interrotta).
<% } %>
					</td>
			  </tr>
			</table>
			<hr>
			<button onclick="if (disableDblClick()) submitForm('doDefault')">Riprova</button>
<% if (bp != null) { %>
			<button onclick="if (disableDblClick()) _submitForm(document.mainForm,'doCloseAll()',true,'GestioneMenu.do');">Chiudi</button>
<% } %>
		</td>
	</tr>
</table>
<% } else { %>	
	<div class="col-md-6 offset-md-3 mx-auto">
		<div class="card">
		  <h3 class="card-header h2 text-danger"><i class="fa fa-exclamation-circle fa-fw fa-2x text-danger" aria-hidden="true"></i> Errore</h3>
		  <div class="card-block p-3">
		    <p class="card-title">
						Operazione in corso.
				  		<br>L'ultima operazione richiesta è ancora in corso.
						<br>Per controllare se l'operazione è terminata premere il bottone 'riprova'.
						<% if (bp != null) { %>
							<br>Per chiudere la pagina premere il bottone 'chiudi' (l'operazione NON verrà interrotta).
						<% } %>
		    <div class="col-sm-offset-5 text-center">
				<button class="btn btn-primary col-md-5" onclick="if (disableDblClick()) submitForm('doDefault')">Riprova</button>
				<button class="btn btn-primary col-md-5" onclick="if (disableDblClick()) _submitForm(document.mainForm,'doCloseAll()',true,'GestioneMenu.do');">Chiudi</button>
			</div>
		  </div>
		</div>
	</div>
<% }%>
</form>
</body>
</html>
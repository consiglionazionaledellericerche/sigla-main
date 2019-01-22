<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.doccont00.consultazioni.bp.*"
%>
<html>
<head>
<% 
  JSPUtils.printBaseUrl(pageContext);
ConsStatoInvioBP bp = (ConsStatoInvioBP)BusinessProcess.getBusinessProcess(request); 
%>
<title>Consultazioni</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript">
function doVisualizzaSingolaContabile(esercizio, cds , numero_mandato) {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Contabile '+esercizio+'-'+cds+'-'+numero_mandato+'.pdf?esercizio='+esercizio+'&cds='+cds+'&numero_mandato='+numero_mandato+'&methodName=scaricaContabile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doVisualizzaContabili() {	
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/contabili.pdf?methodName=scaricaContabili&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
</head>
<body class="Form">
<% bp.openFormWindow(pageContext); %>
<%	if (bp.getParentRoot().isBootstrap()) { %>
	<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
	<% bp.writeHTMLNavigator(out); %>
<% } else {%>
	<table class="Panel" height="100%" width="100%">
		<tr height="100%"><td>
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>
<% } %>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>

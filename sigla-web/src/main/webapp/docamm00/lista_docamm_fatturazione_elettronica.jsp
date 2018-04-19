<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "risultato_ricerca.jsp"
 ?ResourceTimestamp "13/12/00 19.48.42"
 ?ResourceEdition "1.0"
-->

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<%
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<script language="JavaScript">
function doVisualizzaSingoloDocumentiCollegati(esercizio, cd_cds , cd_unita_organizzativa, pg_fattura) {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/DocumentiCollegati '+esercizio+'-'+cd_cds+'-'+cd_unita_organizzativa+'-'+pg_fattura+'.pdf?esercizio='+esercizio+'&cds='+cd_cds+'&cdUo='+cd_unita_organizzativa+'&pgFattura='+pg_fattura+'&methodName=scaricaDocumentiCollegati&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
</head>

<body class="Form">
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
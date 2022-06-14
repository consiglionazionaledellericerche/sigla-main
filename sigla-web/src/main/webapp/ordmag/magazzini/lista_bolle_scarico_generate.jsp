<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.magazzino.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "lista_bolle_scarico_generate.jsp"
 ?ResourceTimestamp "13/12/00 19.48.42"
 ?ResourceEdition "1.0"
-->

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Bolle di Scarico Generate</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<%
ListaBolleScaricoGenerateBP bp = (ListaBolleScaricoGenerateBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext);
%>
<script language="JavaScript">
function doStampaBollaScarico(esercizio, cdCds , cdMagazzino, cdNumeratore, pgBolla) {
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/StampaBollaScarico '+esercizio+'-'+cdCds+'-'+cdMagazzino+'-'+cdNumeratore+'-'+pgBolla+'.pdf?esercizio='+esercizio+'&cds='+cdCds+'&magazzino='+cdMagazzino+'&numeratore='+cdNumeratore+'&pgBolla='+pgBolla+'&methodName=stampaBollaScarico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
</head>
Ciao
<body class="Form">

	<table class="Panel" height="100%" width="100%">
		<tr height="100%"><td>
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>

</html>
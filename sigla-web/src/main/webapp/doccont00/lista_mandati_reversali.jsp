<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>
<html>
<head>
<% 
	JSPUtils.printBaseUrl(pageContext); 
SelezionatoreMandatiInDistintaBP bp = (SelezionatoreMandatiInDistintaBP)BusinessProcess.getBusinessProcess(request);
%>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript">
function doVisualizzaSingoloDocumento(esercizio, cds , uo, numero_documento, tipo) {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Documento contabile '+esercizio+'-'+cds+'-'+numero_documento+'.pdf?esercizio='+esercizio+'&cds='+cds+'&uo='+uo+'&numero_documento='+numero_documento+'&tipo='+tipo+'&methodName=scaricaDocumento&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
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
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>

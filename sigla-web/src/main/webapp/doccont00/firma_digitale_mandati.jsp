<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.reports.bp.*,
			it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% 
	JSPUtils.printBaseUrl(pageContext); 
AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)BusinessProcess.getBusinessProcess(request);
%>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript">
function doVisualizzaSingoloDocumento(esercizio, cds , uo, numero_documento, tipo) {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Documento contabile '+esercizio+'-'+cds+'-'+numero_documento+'.pdf?esercizio='+esercizio+'&cds='+cds+'&uo='+uo+'&numero_documento='+numero_documento+'&tipo='+tipo+'&methodName=scaricaDocumento&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doVisualizzaDocumenti() {
	elementName = "mainTable.selection", mainTableSelection = "";
	for (j = 0;j < document.mainForm.elements.length;j++)
		if (document.mainForm.elements[j].name == elementName && document.mainForm.elements[j].checked)
			mainTableSelection += "&" + elementName + "=" + document.mainForm.elements[j].value;
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Documenti Contabili.pdf?methodName=scaricaDocumenti&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>&mainTable.focus=0' + mainTableSelection);
}
</script>
</head>
<body class="Form">

<% bp.openFormWindow(pageContext); %>
<table class="Panel" width="100%" height="100%">
	<tr>
		<td>
			<fieldset>
			<% bp.writeFormLabel(out,"stato_trasmissione"); %>
			<% bp.writeFormInput(out,null,"stato_trasmissione",false,null,"onchange=\"javascript:submitForm('doCambiaVisibilita')\""); %>
			</fieldset>
		</td>		
	</tr>
	<%	if (!bp.getParentRoot().isBootstrap()) { %>
        <tr height="100%">
            <td colspan="4">
                <% bp.writeHTMLTable(pageContext,"100%","100%"); %>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <% bp.writeHTMLNavigator(out); %>
            </td>
        </tr>
	<% } %>
</table>
<%	if (bp.getParentRoot().isBootstrap()) {
        bp.writeHTMLTable(pageContext,"100%","100%");
        bp.writeHTMLNavigator(out);
    }
%>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
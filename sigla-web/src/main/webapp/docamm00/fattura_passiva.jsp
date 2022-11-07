<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<% CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)BusinessProcess.getBusinessProcess(request); %>
<title> <%=(bp.getModel() instanceof Nota_di_creditoBulk) ?"Nota di Credito":(bp.getModel() instanceof Nota_di_debitoBulk)?"Nota di Debito":"Fattura Passiva"%></title>
<script language="JavaScript">
function doScaricaFatturaHtml() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeFileFirmato()%>.html?methodName=scaricaFatturaHtml&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
</script>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext);
	JSPUtils.tabbed(
					pageContext,
					"tab",
					bp.getTabs(),
					bp.getTab("tab"),
					"center",
					"100%",
					null );
	bp.closeFormWindow(pageContext); %>
</body>
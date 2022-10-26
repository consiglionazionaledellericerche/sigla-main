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
<% CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)BusinessProcess.getBusinessProcess(request); 
	Fattura_attivaBulk fattura = (Fattura_attivaBulk)bp.getModel();
	String nomeFileFirmato = fattura != null?fattura.getNomeFileInvioSdi():"";
%>
<title> <%=(bp.getModel() instanceof Nota_di_credito_attivaBulk) ?"Nota di Credito":(bp.getModel() instanceof Nota_di_debito_attivaBulk)?"Nota di Debito":"Fattura Attiva"%></title>

<script language="JavaScript">
function doVisualizzaDocumentoFatturaElettronica() {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/visualizzaDocumentoAttivo.html?methodName=visualizzaDocumentoAttivo&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Documento', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
function doScaricaFatturaAttivaHtml() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/fatturaAttiva.html?methodName=scaricaFatturaAttivaHtml&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
function doScaricaFatturaAttivaFirmata() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=nomeFileFirmato%>?methodName=scaricaFatturaAttivaFirmata&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
function doRistampaFatturaElettronica() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/ristampaFatturaElettronica.html?methodName=ristampaFatturaElettronica&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}

</script>
</head>
<body class="Form">
<%
    bp.openFormWindow(pageContext);
	JSPUtils.tabbed(
					pageContext,
					"tab",
					bp.getTabs(),
					bp.getTab("tab"),
					"center",
					"100%",
					null );
	bp.closeFormWindow(pageContext);
%>
</body>
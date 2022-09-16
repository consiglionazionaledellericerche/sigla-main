<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.coepcoan00.filter.bulk.*,
		it.cnr.contab.coepcoan00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Partitario Terzo</title>

</head>
<body class="Form">
<%
    BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
	<div class="Group card p-2" style="width:100%">
        <table width="100%">
            <tr><% bp.getController().writeFormField(out,"terzo");%></tr>
            <tr><% bp.getController().writeFormField(out,"cognome");%></tr>
            <tr><% bp.getController().writeFormField(out,"nome");%></tr>
            <tr><% bp.getController().writeFormField(out,"codice_fiscale");%></tr>
            <tr><% bp.getController().writeFormField(out,"partita_iva");%></tr>
            <tr><% bp.getController().writeFormField(out,"dettaglioTributi");%></tr>
        </table>
	</div>
	<% bp.closeFormWindow(pageContext); %>
</body>
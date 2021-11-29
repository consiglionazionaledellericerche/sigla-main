<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.pagopa.bp.CRUDPendenzaPagopaBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% CRUDPendenzaPagopaBP bp = (CRUDPendenzaPagopaBP)BusinessProcess.getBusinessProcess(request);
 PendenzaPagopaBulk bulk = (PendenzaPagopaBulk) bp.getModel();%>

</head>
	<title>Posizioni Debitorie PagoPA</title>
<body class="Form">
<%  bp.openFormWindow(pageContext);%>	 

<div class="Group card p-2">
	<table>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "findTerzo"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "findTerzo"); %>
	</tr>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "codice_fiscale"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "codice_fiscale"); %>
	        <% bp.getController().writeFormLabel( out, "partita_iva"); %>
	        <% bp.getController().writeFormInput( out, "partita_iva"); %>
	</td>
	</tr>
	</table>
</div>

<div class="Group card p-2">
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "descrizione"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "descrizione"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "importoPendenza"); %></td>
	<td colspan=2 >
		<% bp.getController().writeFormInput( out, "importoPendenza"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dtScadenza"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "dtScadenza"); %></td>
	</tr>
	</table>
</div>	

<div class="Group card p-2">
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cdAvviso"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "cdAvviso"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "cdIuv"); %></td>
	<td colspan=2 >
		<% bp.getController().writeFormInput( out, "cdIuv"); %></td>
	</tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
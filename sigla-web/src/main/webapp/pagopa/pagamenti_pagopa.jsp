<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.pagopa.bp.CRUDPagamentoPagopaBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.pagopa.bulk.PagamentoPagopaBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% CRUDPagamentoPagopaBP bp = (CRUDPagamentoPagopaBP)BusinessProcess.getBusinessProcess(request);
 PagamentoPagopaBulk bulk = (PagamentoPagopaBulk) bp.getModel();%>

<script language="JavaScript">
</script>
</head>
	<title>Pagamenti PagoPA</title>
<body class="Form">
<%  bp.openFormWindow(pageContext);%>	 

<div class="Group card p-2">
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "iuv"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "iuv"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ccp"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "ccp"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dtPagamento"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "dtPagamento"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "importo"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "importo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "stato"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "stato"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "causale"); %></td>
	<td colspan=2 >
		<% bp.getController().writeFormInput( out, "causale"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "iur"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "iur"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "rpp"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "rpp"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "id_riconciliazione"); %></td>
	<td colspan=2 >
		<% bp.getController().writeFormInput( out, "id_riconciliazione"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "cd_sospeso"); %></td>
	<td colspan=2 >
		<% bp.getController().writeFormInput( out, "cd_sospeso"); %></td>
	</tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
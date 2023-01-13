<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.logs.bulk.*,
		it.cnr.contab.logs.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione logs</title>
</head>
<body class="Form">

<% CRUDTestataLogBP bp = (CRUDTestataLogBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Group card mb-1 p-2" style="width:100%">
		<tr>
			<td><% bp.getController().writeFormLabel(out,"pg_esecuzione");%></td>
			<td><% bp.getController().writeFormInput(out,"pg_esecuzione");%></td>
			<td><% bp.getController().writeFormLabel(out,"dacr");%></td>
			<td><% bp.getController().writeFormInput(out,"dacr");%></td>
		</tr>
	</table>
	<table class="Group card mb-1 p-2" style="width:100%">
		<tr>
			<td><% bp.getController().writeFormLabel(out,"pg_batch");%></td>
			<td><% bp.getController().writeFormInput(out,"pg_batch");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"pg_job");%></td>
			<td><% bp.getController().writeFormInput(out,"pg_job");%></td>		
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"fl_errori");%></td>
			<td><% bp.getController().writeFormInput(out,"fl_errori");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"ds_log");%></td>
			<td><% bp.getController().writeFormInput(out,"ds_log");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"note");%></td>
			<td><% bp.getController().writeFormInput(out,"note");%></td>
		</tr>
	</table>

    <% bp.getDettagli().writeHTMLTable(
        pageContext,
        null,
        false,
        false,
        false,
        "100%",
        "60vh",
        true); %>

	<table class="Group card p-2" style="width:100%">
		<tr>
			<td><% bp.getDettagli().writeFormLabel(out,"note");%></td>
			<td><% bp.getDettagli().writeFormInput(out,"note");%></td>
		</tr>
		<tr>
			<td><% bp.getDettagli().writeFormLabel(out,"messaggio");%></td>
			<td><% bp.getDettagli().writeFormInput(out,"messaggio");%></td>
		</tr>
		<tr>
			<td><% bp.getDettagli().writeFormLabel(out,"trace");%></td>
			<td><% bp.getDettagli().writeFormInput(out,"trace");%></td>		
		</tr>
	</table>
	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>
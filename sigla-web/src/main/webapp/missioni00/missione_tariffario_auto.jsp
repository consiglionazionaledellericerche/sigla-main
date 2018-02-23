<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.missioni00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Missione Tariffario Auto</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDMissioneTariffarioAutoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table class="Panel">
	<tr>
		<td><% bp.getController().writeFormLabel(out,"cd_tariffa_auto");%></td>
		<td><% bp.getController().writeFormInput(out,"cd_tariffa_auto");%></td>
	</tr> 
	<tr>
		<td><% bp.getController().writeFormLabel(out,"ds_tariffa_auto");%></td>
		<td><% bp.getController().writeFormInput(out,"ds_tariffa_auto");%></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"indennita_chilometrica");%></td>
		<td><% bp.getController().writeFormInput(out,"indennita_chilometrica");%></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
		<td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%>
			<% bp.getController().writeFormLabel(out,"dataFineValidita");%>
			<% bp.getController().writeFormInput(out,"dataFineValidita");%></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_cancellazione");%></td>
		<td><% bp.getController().writeFormInput(out,"dt_cancellazione");%></td>
	</tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
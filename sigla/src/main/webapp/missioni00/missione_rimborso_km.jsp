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
<title>Missione Rimborso Km </title>
</head>
<body class="Form">

<%	CRUDMissioneRimborsoKmBP bp = (CRUDMissioneRimborsoKmBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_auto");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ti_auto");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_area_geografica"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,null,"ti_area_geografica",bp.isEditing(),null,"onChange=\"submitForm('doSelezioneTipoAreaGeografica')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_nazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_nazione");%>
		<% bp.getController().writeFormInput(out,"ds_nazione");%>
		<% bp.getController().writeFormInput(out,"find_nazione");%></td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"indennita_chilometrica");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"indennita_chilometrica");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %>
		<% bp.getController().writeFormLabel(out,"dataFineValidita"); %>
		<% bp.getController().writeFormInput(out,"dataFineValidita");%></td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"dt_cancellazione");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"dt_cancellazione");%></td>
  </tr>

</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
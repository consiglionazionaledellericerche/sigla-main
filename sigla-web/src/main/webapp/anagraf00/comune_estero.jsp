<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Comuni Esteri</title>
</head>
<body class="Form">

<%	CRUDComuneBP bp = (CRUDComuneBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"pg_comune");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_comune");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_nazione");%></td>
	<td><% bp.getController().writeFormInput(out,"pg_nazione");%>
		<% bp.getController().writeFormInput(out,"ds_nazione");%>
		<% bp.getController().writeFormInput(out,"find_nazione");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cap");%></td>
	<td><% bp.getController().writeFormInput(out,"cd_cap");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"cd_comune");%></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_canc"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_canc"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
<!-- 
 ?ResourceName "gestione_conti.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Inquadramenti</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_profilo"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_profilo"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_progressione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_progressione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_inquadramento"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_inquadramento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_dipendente_altro"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_dipendente_altro");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_gruppo_inquadramento"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_gruppo_inquadramento"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
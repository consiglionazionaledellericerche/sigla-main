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
<title>Tipo di rapporto</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_rapporto"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tipo_rapporto"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_tipo_rapporto"); %>
	<td><% bp.getController().writeFormInput(out,"ds_tipo_rapporto"); %>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_dipendente_altro"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"ti_dipendente_altro",false,null,"onClick=\"submitForm('doOnTipoAnagraficoClick')\"");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_rapporto_altro"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_rapporto_altro"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_inquadramento"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_inquadramento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_bonus"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_bonus"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_visibile_a_tutti"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_visibile_a_tutti"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
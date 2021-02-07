<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.bp.*"
		it.cnr.contab.cori00.views.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Consultazione CORI Liquidati</title>

<body class="Form">
<% ConsLiquidCoriBP bp = (ConsLiquidCoriBP)BusinessProcess.getBusinessProcess(request);
   VConsLiqCoriBulk bulk = (VConsLiqCoriBulk)bp.getModel();
   bp.openFormWindow(pageContext); %>


<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_liquidazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio_liquidazione"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds_doc"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds_doc"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_uo_liquidazione"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cd_uo_liquidazione"); %>
		<% bp.getController().writeFormInput(out,"ds_uo_liquidazione"); %>
	</td>
</tr>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzo"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"cdTerzo"); %>
		<% bp.getController().writeFormInput(out,"findTerzo"); %>
	</td>
  </tr>
</table>

<%bp.closeFormWindow(pageContext); %>
</body>
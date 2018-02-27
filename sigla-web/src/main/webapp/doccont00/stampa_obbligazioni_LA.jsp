<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.doccont00.core.bulk.Stampa_obbligazioni_LAVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Stampa Impegni per Gruppo di Azioni Elementari (GAE)</title>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_obbligazioni_LAVBulk bulk = (Stampa_obbligazioni_LAVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
</table>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdrForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdCdrForPrint",(bulk!=null?!bulk.isCdrForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsCdrForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findCdrForPrint",(bulk!=null?!bulk.isCdrForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr></tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findLineaAttForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdLineaAttForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsLineaAttForPrint"); %>
		<% bp.getController().writeFormInput(out,"findLineaAttForPrint"); %>
	</td>
  </tr>
</table>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
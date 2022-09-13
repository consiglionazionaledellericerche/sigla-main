<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa massiva mandato</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
    Stampa_vpg_mandatoBulk bulk = (Stampa_vpg_mandatoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table class="card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOEmittente"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdUOEmittente",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUOEmittente"); %>
		<% bp.getController().writeFormInput(out,null,"findUOEmittente",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
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

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
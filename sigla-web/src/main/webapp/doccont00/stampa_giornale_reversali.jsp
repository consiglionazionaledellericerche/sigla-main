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
</head>
<title>Stampa Giornale delle Reversali</title>
<body class="Form">

<% BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 Stampa_giornale_reversaliBulk bulk = (Stampa_giornale_reversaliBulk)bp.getModel(); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFine"); %></td>
	<td></td>
	<td></td>
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
	<td><% bp.getController().writeFormLabel(out,"ti_reversale"); %></td>
	<td>
	<% bp.getController().writeFormLabel(out,"accreditamento"); %>
	<% bp.getController().writeFormInput(out,"accreditamento"); %></td>
	<td>
	<% bp.getController().writeFormLabel(out,"reversale"); %>
	<% bp.getController().writeFormInput(out,"reversale"); %></td>
	<td>
	<% bp.getController().writeFormLabel(out,"regolarizzazione"); %>
	<% bp.getController().writeFormInput(out,"regolarizzazione"); %></td>
	<td>
	<% bp.getController().writeFormLabel(out,"sospeso"); %>
	<% bp.getController().writeFormInput(out,"sospeso"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato"); %></td>
	<td colspan=5><% bp.getController().writeFormInput(out,"stato"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_trasmissione"); %></td>
	<td colspan=5><% bp.getController().writeFormInput(out,"stato_trasmissione"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
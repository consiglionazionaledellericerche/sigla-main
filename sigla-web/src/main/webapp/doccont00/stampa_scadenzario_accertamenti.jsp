<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.doccont00.core.bulk.Stampa_scadenzario_accertamentiBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Stampa Scadenzario Accertamenti</title>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_scadenzario_accertamentiBulk bulk = (Stampa_scadenzario_accertamentiBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdsForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdCdsForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsCdsForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findCdsForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
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
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_documento_cont"); %></td>
	<td colspan=5><% bp.getController().writeFormInput(out,"cd_tipo_documento_cont"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"riporto"); %></td>
	<td colspan=5><% bp.getController().writeFormInput(out,"riporto"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
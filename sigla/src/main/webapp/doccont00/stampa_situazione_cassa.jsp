<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.intcass.bulk.Stampa_vpg_situazione_cassaVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa situazione di cassa</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_vpg_situazione_cassaVBulk bulk = (Stampa_vpg_situazione_cassaVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table class="w-100 card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td colspan="5">
		<% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUoForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_stato"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_stato"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
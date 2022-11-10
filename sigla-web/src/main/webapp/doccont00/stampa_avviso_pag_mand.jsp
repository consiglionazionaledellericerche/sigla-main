<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*,
	it.cnr.contab.doccont00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa avviso pagamento mandati</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	Stampa_avviso_pag_mandBulk bulk = (Stampa_avviso_pag_mandBulk)bp.getModel(); %>

<table class="card">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
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
	<td><% bp.getController().writeFormLabel(out,"pgInizioMand"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizioMand"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFineMand"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFineMand"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizioDist"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizioDist"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFineDist"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFineDist"); %></td>
	<td></td>
	<td></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
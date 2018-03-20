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
<title>Stampa Fondo Economale</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
<div class="card p-2 w-100">
<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdCDSForPrint"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"cdCDSForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdUOForPrint"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"cdUOForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
	</td>
  </tr>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fondoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_fondoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_fondoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"fondoForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
</table>
</div>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>
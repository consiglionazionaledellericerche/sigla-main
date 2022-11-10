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
<title>Stampa Indice Tempestività Pagamenti</title>
</head>
<body class="Form">
<%
    BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<table class="card p-2">
  <tr><% bp.getController().writeFormField(out,"esercizio"); %></tr>
  <tr><% bp.getController().writeFormField(out,"trimestre"); %></tr>
  <tr><% bp.getController().writeFormField(out,"dettagli"); %></tr>
</table>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>
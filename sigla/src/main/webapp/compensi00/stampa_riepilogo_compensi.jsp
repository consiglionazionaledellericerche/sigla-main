<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa riepilogo compensi</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_unita_organizzativa"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_unita_organizzativa"); %></td>
  </tr>
</table>
  <br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzoForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_terzo"); %>
		<% bp.getController().writeFormInput(out,"denominazione_sede"); %>
		<% bp.getController().writeFormInput(out,"findTerzoForPrint"); %>
	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
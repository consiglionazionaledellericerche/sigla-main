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
<title>Stampa Impegni / Documenti Ammnistrativi</title>
</head>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);  %>
<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdsForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findCdsForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findUoForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findElementoVoceForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findElementoVoceForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findEsercizioDocForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findEsercizioDocForPrint"); %></td>
  </tr>
</table> 

</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
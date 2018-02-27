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
<title>Stampa libro Giornale</title>
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
	<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findUOForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"tipologia"); %></td>
	<td><% bp.getController().writeFormInput(out,"tipologia"); %></td>
  </tr>
  <tr>
	<td colspan=2><% bp.getController().writeFormLabel(out,"da_contabilizzazione"); %>
		<% bp.getController().writeFormInput(out,"da_contabilizzazione"); %>
		<% bp.getController().writeFormLabel(out,"a_contabilizzazione"); %>
		<% bp.getController().writeFormInput(out,"a_contabilizzazione"); %></td>
  </tr>
</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
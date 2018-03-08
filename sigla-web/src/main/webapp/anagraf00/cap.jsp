<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>C.A.P.</title>
</head>
<body class="Form">

<%	CRUDCapBP bp = (CRUDCapBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_comune"); %>
		<% bp.getController().writeFormInput(out,"ds_comune"); %>
		<% bp.getController().writeFormInput(out,"find_comune"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cap"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cap"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>
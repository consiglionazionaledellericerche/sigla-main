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
<title>Comuni Italiani</title>
</head>
<body class="Form">

<%	CRUDComuneBP bp = (CRUDComuneBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_comune"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_comune"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_comune"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_catastale"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_catastale"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cap"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cap"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_provincia"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_provincia"); %>
		<% bp.getController().writeFormInput(out,"ds_provincia"); %>
		<% bp.getController().writeFormInput(out,"find_provincia"); %></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_canc"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_canc"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>
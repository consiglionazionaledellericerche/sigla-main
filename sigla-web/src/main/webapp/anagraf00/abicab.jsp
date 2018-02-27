<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Abi/Cab</title>
</head>
<body class="Form">

<%	CRUDAbiCabBP bp = (CRUDAbiCabBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"abi"); %></td>
	<td><% bp.getController().writeFormInput(out,"abi"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cab"); %></td>
	<td><% bp.getController().writeFormInput(out,"cab"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_abicab"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_abicab"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"via"); %></td>
	<td><% bp.getController().writeFormInput(out,"via"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"frazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"frazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_comune"); %>
		<% bp.getController().writeFormInput(out,"ds_comune"); %>
		<% bp.getController().writeFormInput(out,"find_comune"); %></td>
  </tr>
  <% if (bp.isSearching()) { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"capForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,"capForSearch"); %></td>
  </tr>
  <% } else { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cap"); %></td>
	<td><% bp.getController().writeFormInput(out,"cap"); %></td>
  </tr>
  <% } %>
  <tr>
	<td>
		<% bp.getController().writeFormLabel(out,"fl_cancellato"); %>
		<% bp.getController().writeFormInput(out,"fl_cancellato"); %>
	</td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.tabrif.bulk.*,
		it.cnr.contab.compensi00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Associazione Gruppo Contributo Ritenuta - UO</title>
</head>
<body class="Form">

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">
 
 <tr>
	<td><% bp.getController().writeFormLabel(out,"gruppo"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_gruppo_cr"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_gruppo_cr"); %></td>
	<td><% bp.getController().writeFormInput(out,"gruppo"); %></td>				 
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"unita_organizzativa"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"unita_organizzativa"); %></td>				 
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_accentrato"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_accentrato"); %></td>
  </tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
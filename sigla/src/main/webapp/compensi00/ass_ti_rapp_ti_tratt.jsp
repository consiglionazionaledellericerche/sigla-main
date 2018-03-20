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
<title>Associazione Tipo Rapporto/Tipo Trattamento</title>
</head>
<body class="Form">

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">

  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_rapporto"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tipo_rapporto"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_tipo_rapporto"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_tipo_rapporto"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_trattamento"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_trattamento"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_trattamento"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_trattamento"); %></td>
  </tr>
	
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
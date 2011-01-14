<!-- 
 ?ResourceName "tipo_variazione.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); 
   CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>
<body class="Form">

<%bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_variazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tipo_variazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_tipo_variazione"); %>
	<td><% bp.getController().writeFormInput(out,"ds_tipo_variazione"); %>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_tipo_variazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_tipo_variazione");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_utilizzabile_ente"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_utilizzabile_ente"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_utilizzabile_area"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_utilizzabile_area"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_utilizzabile_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_utilizzabile_cds"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_approvazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_approvazione"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
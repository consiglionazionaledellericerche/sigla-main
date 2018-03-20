<!-- 
 ?ResourceName "tipo_variazione.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg01.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); 
   CRUDTipoVariazioneBP bp = (CRUDTipoVariazioneBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>
<body class="Form">

<%bp.openFormWindow(pageContext); %>

<table class="Panel card m-2 p-2">
  <tr><% bp.getController().writeFormField(out,"cd_tipo_variazione"); %></tr>
  <tr><% bp.getController().writeFormField(out,"ds_tipo_variazione"); %></tr>
  <tr><% bp.getController().writeFormField(out,"ti_tipo_variazione"); %></tr>
  <tr><% bp.getController().writeFormField(out,"fl_utilizzabile_ente"); %></tr>
  <tr><% bp.getController().writeFormField(out,"fl_utilizzabile_area"); %></tr>
  <tr><% bp.getController().writeFormField(out,"fl_utilizzabile_cds"); %></tr>
  <% if (bp.isFlVariazioniTrasferimento()) { %>
  <tr><% bp.getController().writeFormField(out,"fl_variazione_trasferimento"); %></tr>
  <% } %>
  <tr><% bp.getController().writeFormField(out,"ti_approvazione"); %></tr>
</table>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
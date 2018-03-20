<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Associazione Tipo CORI/Elemento Voce</title>
</head>
<body class="Form">

<% 	CRUDAssTipoCoriEvBP bp = (CRUDAssTipoCoriEvBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">

  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"ds_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"find_contributo_ritenuta"); %></td>				 
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_elemento_voce"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"ds_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"find_elemento_voce"); %></td>				 
  </tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"ti_ente_percepiente"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_ente_percepiente"); %>
	  </tr>
<tr>
		<td><% bp.getController().writeFormLabel(out,"ti_gestione"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_gestione"); %>
	  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
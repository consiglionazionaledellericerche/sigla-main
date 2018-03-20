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
<title>Associazione Tipo CORI - Voce E/P</title>
</head>
<body class="Form">
<% 	CRUDAssTipoCoriVEPBP bp = (CRUDAssTipoCoriVEPBP)BusinessProcess.getBusinessProcess(request);
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
	<td><% bp.getController().writeFormLabel(out,"find_voce"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_voce_ep"); %>
		<% bp.getController().writeFormInput(out,"ds_voce_ep"); %>
		<% bp.getController().writeFormInput(out,"find_voce"); %></td>				 
  </tr>
    <tr>
	<td><% bp.getController().writeFormLabel(out,"find_voce_contr"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_voce_ep_contr"); %>
		<% bp.getController().writeFormInput(out,"ds_voce_ep_contr"); %>
		<% bp.getController().writeFormInput(out,"find_voce_contr"); %></td>				 
  </tr>
  
	<tr>
		<td><% bp.getController().writeFormLabel(out,"ti_ente_percepiente"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_ente_percepiente"); %>
	  </tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"sezione"); %></td>
		<td><% bp.getController().writeFormInput(out,"sezione"); %>
	  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
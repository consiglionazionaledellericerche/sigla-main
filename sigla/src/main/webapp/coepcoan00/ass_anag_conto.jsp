<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

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
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Associazione anagrafica conto</title>
<body class="Form">
<% it.cnr.contab.coepcoan00.bp.CRUDAssAnagContoBP bp = (it.cnr.contab.coepcoan00.bp.CRUDAssAnagContoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "esercizio"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	
	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "ti_terzo"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "ti_terzo"); %></td>
	</tr>

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "italiano_estero"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "italiano_estero"); %></td>
	</tr>

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "ti_entita"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "ti_entita"); %></td>
	</tr>

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "ente_altro"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "ente_altro"); %></td>
	</tr>

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "cd_classific_anag"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "cd_classific_anag"); %>
				<% bp.getController().writeFormInput( out, "ds_classanag"); %>
				<% bp.getController().writeFormInput( out, "find_classanag"); %></td>
	</tr>

	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "cd_voce_ep"); %></td>
		<td>	<% bp.getController().writeFormInput( out, "cd_voce_ep"); %>
				<% bp.getController().writeFormInput( out, "ds_conto"); %>
				<% bp.getController().writeFormInput( out, "find_conto"); %></td>
	</tr>
	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
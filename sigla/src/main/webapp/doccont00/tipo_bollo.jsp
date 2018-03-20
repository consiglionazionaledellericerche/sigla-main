<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Tipo Bollo</title>
<body class="Form">
<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_tipo_bollo"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_tipo_bollo"); %>
		<% bp.getController().writeFormLabel( out, "fl_default"); %>
		<% bp.getController().writeFormInput( out, "fl_default"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ds_tipo_bollo"); %></td>	
		<td><% bp.getController().writeFormInput( out, "ds_tipo_bollo"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "classe_tipo_bollo"); %></td>	
		<td><% bp.getController().writeFormInput( out, "classe_tipo_bollo"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ti_entrata_spesa"); %></td>	
		<td><% bp.getController().writeFormInput( out, "ti_entrata_spesa"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "im_tipo_bollo"); %></td>	
		<td><% bp.getController().writeFormInput( out, "im_tipo_bollo"); %></td>
	</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
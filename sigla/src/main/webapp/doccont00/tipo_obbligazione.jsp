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
<title>Tipo Impegno</title>
<body class="Form">
<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_tipo_obbligazione"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "cd_tipo_obbligazione"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_tipo_obbligazione"); %></td>	
	<td><% bp.getController().writeFormInput( out, "ds_tipo_obbligazione"); %></td>
	</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
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
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Associazione Titolo Spesa CNR - Titolo Spesa CDS</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>
	<td>
	    <% bp.getController().writeFormInput( out, "cd_elemento_voce"); %>
	    <% bp.getController().writeFormInput( out, "ds_elemento_voce"); %>
      <% bp.getController().writeFormInputByStatus( out, "find_elemento_voce"); %>	
	</td>				 
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_coll"); %></td>
	<td>
	    <% bp.getController().writeFormInput( out, "cd_elemento_voce_coll"); %>
	    <% bp.getController().writeFormInput( out, "ds_elemento_voce_coll"); %>
      <% bp.getController().writeFormInputByStatus( out, "find_elemento_voce_coll"); %>	
	</td>				 
	</tr>
	<td></td>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
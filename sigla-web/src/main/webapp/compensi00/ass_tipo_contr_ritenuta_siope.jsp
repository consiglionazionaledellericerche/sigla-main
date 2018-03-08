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
<title>Associazione Tipo Contributo Ritenuta - Codice Siope</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">

	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_contributo_ritenuta"); %></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_contributo_ritenuta"); %>
	    	<% bp.getController().writeFormInput( out, "ds_contributo_ritenuta"); %>
      		<% bp.getController().writeFormInput( out, "find_contributo_ritenuta"); %>	
		</td>				 
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_siope_s"); %></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_siope_s"); %>
	    	<% bp.getController().writeFormInput( out, "ds_siope_s"); %>
      		<% bp.getController().writeFormInput( out, "find_siope_spesa"); %>	
		</td>				 
	</tr>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_siope_e"); %></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_siope_e"); %>
	    	<% bp.getController().writeFormInput( out, "ds_siope_e"); %>
      		<% bp.getController().writeFormInput( out, "find_siope_entrata"); %>	
		</td>				 
	</tr>
	
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
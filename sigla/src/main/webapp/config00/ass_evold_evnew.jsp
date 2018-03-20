<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk,
it.cnr.contab.config00.bp.CRUDConfigAssEvoldEvnewBP"%>
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
<title>Associazione Voce Vecchia - Voce Nuova</title>
<body class="Form">

<% CRUDConfigAssEvoldEvnewBP bp = (CRUDConfigAssEvoldEvnewBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ti_gestione_search"); %></td>
		<td><% bp.getController().writeFormInput( out, "ti_gestione_search"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_old"); %>
		<label> <%=((Ass_evold_evnewBulk)bp.getModel()).getEsercizio_old_search().toString()%></label></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_elemento_voce_old"); %>
	    	<% bp.getController().writeFormInput( out, "ds_elemento_voce_old"); %>
      		<% bp.getController().writeFormInput( out, "find_elemento_voce_old"); %>	
		</td>				 
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_old"); %>
		<label> <%=((Ass_evold_evnewBulk)bp.getModel()).getEsercizio_new_search().toString()%></label></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_elemento_voce_new"); %>
	    	<% bp.getController().writeFormInput( out, "ds_elemento_voce_new"); %>
      		<% bp.getController().writeFormInput( out, "find_elemento_voce_new"); %>	
		</td>				 
	</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
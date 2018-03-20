<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Utenza Amministratore</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_utente"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "cd_utente"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_utente_uid"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_utente_uid"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_utente"); %></td>
	<td><% bp.getController().writeFormInput( out, "ds_utente"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "nome"); %></td>
	<td><% bp.getController().writeFormInput( out, "nome"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cognome"); %></td>	
	<td><% bp.getController().writeFormInput( out, "cognome"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "indirizzo"); %></td>
	<td><% bp.getController().writeFormInput( out, "indirizzo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_inizio_validita"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_fine_validita"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_fine_validita"); %></td>
	</tr>
	<tr>
	<td><% JSPUtils.button(out,bp.encodePath("img/cut24.gif"),bp.encodePath("img/cut24.gif"),"Annulla password","javascript:submitForm('doResetPassword')",bp.isEditing(),bp.getParentRoot().isBootstrap()); %></td>
	<td><% bp.getController().writeFormInput( out, "fl_password_change"); %>
			<% bp.getController().writeFormLabel( out, "fl_password_change"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cds_configuratore_cd"); %></td>
	<td>
	   <% bp.getController().writeFormInputByStatus( out, "cds_configuratore_cd"); 
	       bp.getController().writeFormInputByStatus( out, "cds_configuratore_ds"); %>
		 <% bp.getController().writeFormInputByStatus( out, "find_cds"); %>				 	
	</td>
	</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
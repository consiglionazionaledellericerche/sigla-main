<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.contab.segnalazioni00.bp.*,
	        it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Attivita</title>
<body class="Form">

<% CRUDAttivitaSiglaBP bp = (CRUDAttivitaSiglaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr> 
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
			<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
			<td><% bp.getController().writeFormLabel( out, "pg_attivita"); %></td>
			<td><% bp.getController().writeFormInput( out, "pg_attivita"); %></td>
		</tr>
		<tr>	
			<td><% bp.getController().writeFormLabel( out, "protocollo"); %></td>
			<td><% bp.getController().writeFormInput( out, "protocollo"); %></td>
			<td><% bp.getController().writeFormLabel( out, "id_help"); %></td>
			<td><% bp.getController().writeFormInput( out, "id_help"); %></td>
			<td><% bp.getController().writeFormLabel( out, "fl_bug_aperto"); %>
				<% bp.getController().writeFormInput( out, "fl_bug_aperto"); %></td>
			<td><% bp.getController().writeFormLabel( out, "id_bugzilla"); %></td>
			<td><% bp.getController().writeFormInput( out, "id_bugzilla"); %></td>
		</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "dt_attivita"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "dt_attivita"); %></td>
		<td><% bp.getController().writeFormLabel( out, "richiedente"); %></td>
		<td colspan="4"><% bp.getController().writeFormInput( out, "richiedente"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ds_attivita"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "ds_attivita"); %></td>
		<td><% bp.getController().writeFormLabel( out, "note"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "note"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "dt_consegna"); %></td>
		<td><% bp.getController().writeFormInput( out, "dt_consegna"); %></td>
		<td><% bp.getController().writeFormLabel( out, "tipo_attivita"); %></td>
		<td><% bp.getController().writeFormInput( out, "tipo_attivita"); %></td>
		
	</tr>
	<tr>	
		<td><% bp.getController().writeFormLabel( out, "priorita"); %></td>
		<td><% bp.getController().writeFormInput( out, "priorita"); %></td>
		<td><% bp.getController().writeFormLabel( out, "note_priorita"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "note_priorita"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "terzo"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_redattore"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo"); %>
			<% bp.getController().writeFormInput( out, "terzo"); %></td>
		
		<td colspan="2"> <% bp.getController().writeFormLabel( out, "stato"); %>
			<% bp.getController().writeFormInput( out, "stato"); %></td>
	</tr>
	<tr>		
		<td ><% bp.getController().writeFormLabel( out, "dt_inizio_attivita"); %>
		<td><% bp.getController().writeFormInput( out, "dt_inizio_attivita"); %>
		<td><% bp.getController().writeFormLabel( out, "dt_fine_attivita"); %>
		<td><% bp.getController().writeFormInput( out, "dt_fine_attivita"); %>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "referenti"); %></td>
		<td colspan="2">	<% bp.getController().writeFormInput( out, "referenti"); %></td>
		<td colspan="2">	<% bp.getController().writeFormLabel( out, "tempo_analisi"); %>
			<% bp.getController().writeFormInput( out, "tempo_analisi"); %></td>
		<td colspan="2">	<% bp.getController().writeFormLabel( out, "tempo_sviluppo"); %>
			<% bp.getController().writeFormInput( out, "tempo_sviluppo"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "terzo1"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_responsabile_1"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo1"); %>
			<% bp.getController().writeFormInput( out, "terzo1"); %></td>
					
		<td><% bp.getController().writeFormLabel( out, "terzo2"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_responsabile_2"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo2"); %>
			<% bp.getController().writeFormInput( out, "terzo2"); %></td>
	</tr>
	<tr>	
		<td><% bp.getController().writeFormLabel( out, "terzo3"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_responsabile_3"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo3"); %>
			<% bp.getController().writeFormInput( out, "terzo3"); %></td>
		<td><% bp.getController().writeFormLabel( out, "terzo4"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_responsabile_4"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo4"); %>
			<% bp.getController().writeFormInput( out, "terzo4"); %></td>
	</tr>
	<tr>		
		<td><% bp.getController().writeFormLabel( out, "terzo5"); %></td>
		<td colspan="3"> <% bp.getController().writeFormInput( out, "cd_responsabile_5"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo5"); %>
			<% bp.getController().writeFormInput( out, "terzo5"); %></td>
		
		<td><% bp.getController().writeFormLabel( out, "terzo6"); %></td>
		<td colspan="3"><% bp.getController().writeFormInput( out, "cd_responsabile_6"); %>
			<% bp.getController().writeFormInput( out, "ds_terzo6"); %>
			<% bp.getController().writeFormInput( out, "terzo6"); %></td> 
	</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
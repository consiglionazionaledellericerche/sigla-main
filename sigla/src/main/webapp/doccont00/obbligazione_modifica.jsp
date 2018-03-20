<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Variazioni agli Impegni residui</title>
<body class="Form">


<%  
		CRUDObbligazioneModificaBP bp = (CRUDObbligazioneModificaBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
		it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk obbligazione_modifica = (it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk)bp.getModel();
%>

<div class="Group card p-3 m-1 w-100">		
<table>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_modifica"); %></td>
			<td><% bp.getController().writeFormInput( out, "pg_modifica"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "dt_modifica"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_modifica"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "esercizio_originale"); %></td>
			<td><% bp.getController().writeFormInput( out, "esercizio_originale"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>
			<td><% bp.getController().writeFormInput( out, "pg_obbligazione"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "ds_modifica"); %></td>
			<td class="w-100"><% bp.getController().writeFormInput( out, "ds_modifica"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "motivazione"); %></td>
			<td class="w-100"><% bp.getController().writeFormInput( out, "motivazione"); %></td>
		</tr>
</table>
</div>

<div class="Group card p-3 m-1">
	<table class="w-100">
		<tr>
			<td colspan=2 class="w-100">
			     <% // Visualizzazione dettagli modifica obbligazione
		      		bp.getDettagliModifica().writeHTMLTable(pageContext,"obbligazioneModifica",false,false,false,"100%","200px");
		      	%>
			</td>
		</tr>
	</table>
<!--
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td><% bp.getDettagliModifica().writeFormLabel(out,"cd_voce"); %></td>
			<td><% bp.getDettagliModifica().writeFormInput(out,"cd_voce"); %>
				<% bp.getDettagliModifica().writeFormInput(out,"ds_voce"); %>
				<% bp.getDettagliModifica().writeFormInput(out,"find_voce"); %></td>
		</tr>
		<tr>
			<td><% bp.getDettagliModifica().writeFormLabel(out,"find_linea_attivita");%></td>
			<td><% bp.getDettagliModifica().writeFormInput(out,"find_linea_attivita");%></td>
		</tr>
		<tr>
			<td><% bp.getDettagliModifica().writeFormLabel(out,"im_modifica");%></td>
			<td><% bp.getDettagliModifica().writeFormInput(out,"im_modifica");%></td>
		</tr>
	</table>
-->
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
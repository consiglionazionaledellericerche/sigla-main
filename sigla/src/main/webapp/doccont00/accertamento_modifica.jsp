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
<title>Variazioni agli Accertamenti residui</title>
<body class="Form">


<%  
		CRUDAccertamentoModificaBP bp = (CRUDAccertamentoModificaBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
		it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk accertamento_modifica = (it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk)bp.getModel();
%>

<div class="Group">		
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
			<td><% bp.getController().writeFormLabel( out, "pg_accertamento"); %></td>
			<td><% bp.getController().writeFormInput( out, "pg_accertamento"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "ds_modifica"); %></td>
			<td><% bp.getController().writeFormInput( out, "ds_modifica"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_variazione"); %></td>
			<td>
				<table>
					<tr>
						<td><% bp.getController().writeFormInput( out, "pg_variazione"); %></td>
						<td><% bp.getController().writeFormInput( out, "variazioni"); %></td>
					</tr>
				</table>
			<td>
		</tr>
</table>
</div>

<div class="Group">
	<table>
		<tr>
			<td colspan=2>
			     <% // Visualizzazione dettagli modifica accertamento
		      		bp.getDettagliModifica().writeHTMLTable(pageContext,"accertamentoModifica",false,false,false,"100%","200px");
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
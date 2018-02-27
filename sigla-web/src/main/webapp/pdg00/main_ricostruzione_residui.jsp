<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ((CRUDRicostruzioneResiduiBP)bp).getCrudDettagli();
%>

	<title>Gestione Ricostruzione dei residui</title>

<body class="Form">

<% bp.openFormWindow(pageContext); %>

<div class="Group">
	<table>
		<td>
			<table class="Panel">
				<tr>
				<td>	<% bp.getController().writeForm(out); %></td>
				</tr>
			</table>
		</td>
		<td>
			<% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Consultazione residui inseriti","javascript:submitForm('doVisualizzaRicosResidui')", null, "Visualizza i dettagli della ricostruzione residui", true, bp.getParentRoot().isBootstrap()); %>
		</td>
	</table>
</div>

<div class="Group">
	<table border="0" cellspacing="0" cellpadding="0">
	<td>
	<%	controller.writeHTMLTable(pageContext,"dett",true,true,true,"100%","180px"); %>
	</td>

<div class="Group">
	<table border="0" cellspacing="0" cellpadding="2">
	<tr>
	<td><% controller.writeFormLabel(out, "stato"); %></td>
    <td><% controller.writeFormInput(out, "stato"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "centro_responsabilita_la"); %></td>
	<td><% controller.writeFormInput(out, "centro_responsabilita_la"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "linea_attivita"); %></td>
	<td><% controller.writeFormInput(out, "linea_attivita"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "cd_progetto"); %></td>
    <td><% controller.writeFormInput(out, "cd_progetto"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "cd_dipartimento"); %></td>
	<td><% controller.writeFormInput(out, "cd_dipartimento"); %></td>				 
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "elemento_voce"); %></td>
    <td><% controller.writeFormInput(out, "elemento_voce"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "cd_classificazione_spese"); %></td>
    <td><% controller.writeFormInput(out, "cd_classificazione_spese"); %>
        <% controller.writeFormInput(out, "ds_classificazione_spese"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "dt_registrazione"); %></td>
    <td><% controller.writeFormInput(out, "dt_registrazione"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "descrizione"); %></td>
    <td><% controller.writeFormInput(out, "descrizione"); %></td>
	</tr>
	<tr>
	<td><% controller.writeFormLabel(out, "im_residuo"); %></td>
    <td><% controller.writeFormInput(out, "im_residuo"); %></td>
	</tr>

    </table>
</div>

<%bp.closeFormWindow(pageContext); %>
</body>
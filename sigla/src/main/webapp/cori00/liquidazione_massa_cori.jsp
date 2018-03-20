<!-- 
 ?ResourceName "liquidazione_massa_cori.jsp"
 ?ResourceTimestamp "12/12/05 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Liquidazione CORI massiva</title>
</head>
<body class="Form">

<% LiquidazioneMassaCoriBP bp = (LiquidazioneMassaCoriBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
		<fieldset>
		<legend class="GroupLabel">Parametri di Selezione</legend>
		<table width="100%">

			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"da_esercizio_precedente");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"da_esercizio_precedente",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"data_da");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_da",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"data_a");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_a",false,null,"");%>
				</td>
			</tr>
		</table>
		</fieldset>
		<fieldset>
		<legend class="GroupLabel">Risultato dell'Elaborazione</legend>
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"pg_exec");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"pg_exec",false,null,"");%>
				</td>
				<td>
					<% JSPUtils.button(out,"img/find24.gif", "Ricerca","javascript:submitForm('doCercaBatch')", bp.getParentRoot().isBootstrap()); %>
				</td>
			</tr>
			<table width="100%">
				<tr>
					<td>
						<% bp.getController().writeFormInput(out,null,"note",false,null,"");%>
					</td>
				</tr>
			</table>	
				<table width="100%">
					<tr>
					  <td colspan="4">
						<% bp.getBatch_log_riga().writeHTMLTable(
							pageContext,
							"LIQUID_CORI_MASSA_COLUMN_SET",
							false,
							false,
							false,
							"100%",
							"200px"); %>
					  </td>
					</tr>
				</table>
		</table>
		</fieldset>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
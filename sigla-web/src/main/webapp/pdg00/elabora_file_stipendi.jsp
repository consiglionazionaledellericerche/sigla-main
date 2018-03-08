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
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Elaborazione Flussi Stipendi</title>
</head>

<body class="Form">
<% ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
	<fieldset>
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>	
		<table width="100%">
			<tr>
			  	<td colspan="4">
					<% bp.getV_stipendi_cofi_dett().writeHTMLTable(
						pageContext,
						"default",
						false,
						false,
						false,
						"100%",
						"180px"); %>
				</td>
			</tr>
		</table>
	</fieldset>
	</div>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td>
   	    		  <span style="font-weight:bold; font-family:sans-serif; font-size:16px; color:blue">Risultato dell'Elaborazione</span>
 				</td>    
 				<td>
				  <% JSPUtils.button(out,"img/history24.gif", "Visualizza<br>Flusso <u>E</u>ntrata","javascript:submitForm('doVisualizzaEntrata')", bp.getParentRoot().isBootstrap()); %>
				</td>
				<td>	
				  <% JSPUtils.button(out,"img/history24.gif", "Visualizza<br>Flusso <u>S</u>pesa","javascript:submitForm('doVisualizzaSpesa')", bp.getParentRoot().isBootstrap()); %>
				</td>
			</tr>
		</table>			
	<fieldset>
		<table width="100%">
			<tr>
			  <td colspan="4">
				<% bp.getBatch_log_riga().writeHTMLTable(
					pageContext,
					"ELAB_FILE_STIP_COLUMN_SET",
					false,
					false,
					false,
					"100%",
					"200px"); %>
			  </td>
			</tr>
		</table>
	</fieldset>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 
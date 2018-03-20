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
<title>Stampa Attività Sigla</title>
</head>
<body class="Form"> 

<%	it.cnr.contab.reports.bp.ParametricPrintBP bp = (it.cnr.contab.reports.bp.ParametricPrintBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
<div class="Group">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio_base"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio_base"); %></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findTerzoForPrint"); %></td>
  </tr>
</table>

<table>
 <tr> 
  <td>
  	<div class="Group">
     <table>
   		
	  <tr>
		<td class="GroupLabel">Stato</td>
		<td><% bp.getController().writeFormInput(out,"selezionaStato"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_iniziale"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_iniziale"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_analisi"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_analisi"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_sviluppo"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_sviluppo"); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_test"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_test"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_rilasciato"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_rilasciato"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"stato_differito"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_differito"); %></td>
	  </tr>
	  
	 </table>
	 <table> 
	 
	  <tr>
		<td class="GroupLabel">Tipo Attivita</td>
		<td><% bp.getController().writeFormInput(out,"selezionaTipoAttivita"); %></td>
	  </tr>	    
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_att_correttiva"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_att_correttiva"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_att_evolutiva"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_att_evolutiva"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_att_manutentiva"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_att_manutentiva"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_att_estrazione_dati"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_att_estrazione_dati"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_att_supporto_gestione"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_att_supporto_gestione"); %></td>
	  </tr>
	  
	   </table>
	 </div>
	</td>
   </tr>	
</table>  	
</div>   


<% bp.closeFormWindow(pageContext); %>
</body>
</html>
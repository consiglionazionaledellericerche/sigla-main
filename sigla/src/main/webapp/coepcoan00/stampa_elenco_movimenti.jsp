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
<title>Stampa Elenco Movimenti per Conto</title>
</head>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);  %>
<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findUOForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzoForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,"findTerzoForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"attiva"); %></td>
	<td><% bp.getController().writeFormInput(out,"attiva"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findContoForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_voce_ep"); %>
		<% bp.getController().writeFormInput(out,"ds_voce_ep"); %>
		<% bp.getController().writeFormInput(out,"findContoForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"tipologia"); %></td>
	<td><% bp.getController().writeFormInput(out,"tipologia"); %></td>
  </tr>
  <table>  
  <tr>
   <td>	
	<div class="Group">
	<table>    	  
	  <tr>
		<td class="GroupLabel">Raggruppamenti</td>
		<td><% bp.getController().writeFormInput(out,"seleziona"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_causale"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_causale"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_chiusura"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_chiusura"); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_doc_amm"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_doc_amm"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_doc_cont"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_doc_cont"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_liquid_iva"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_liquid_iva"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_mig_beni"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_mig_beni"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_stipendi"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_stipendi"); %></td>
	  </tr>
	  </table>
	 </div>
	</td>
   </tr>	    
</table> 
</table> 
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
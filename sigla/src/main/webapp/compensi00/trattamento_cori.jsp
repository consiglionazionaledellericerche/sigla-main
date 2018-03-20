<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,		
		it.cnr.contab.compensi00.tabrif.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Trattamento CORI</title>
</head>
<body class="Form">

<% 	CRUDTrattamentoCORIBP bp = (CRUDTrattamentoCORIBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	Trattamento_coriBulk trattCORI = (Trattamento_coriBulk)bp.getModel(); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_trattamento"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_trattamento"); %></td>
	<td colspan=2><% bp.getController().writeFormInput(out,"ds_trattamento"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_trattamento"); %></td>
  </tr>
</table>

<table class="Group" width=100%>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"ds_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"find_contributo_ritenuta"); %></td>
  </tr>			
  <tr>
	<td><% bp.getController().writeFormLabel(out,"segno"); %></td>
	<td><% bp.getController().writeFormInput(out,"segno"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"algoritmo"); %></td>
	<td colspan=2><% bp.getController().writeFormInput(out,"algoritmo"); %></td>
<!--	<td><% JSPUtils.button(out,null,null,"Inserisci Riga","if (disableDblClick()) submitForm('doAggiungiRiga')",null,true, bp.getParentRoot().isBootstrap());%></td> -->
  </tr>			
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %>
		<% bp.getController().writeFormLabel(out,"dataFineValidita"); %>
		<% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
  </tr>
</table>

<table>
  <tr>
	<td colspan = "4">
		<% bp.getRigheCRUDController().writeHTMLTable(
				pageContext,
				"righe",
				false,
				false,
				false,
				"100%",
				"200px",
				true); %>
	</td>
  </tr>
</table>


<% bp.closeFormWindow(pageContext); %>
</body>
</html>
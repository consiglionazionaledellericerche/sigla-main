<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.doccont00.bp.*,
			it.cnr.contab.doccont00.core.bulk.Stampa_registro_annotazione_spese_pgiroBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<% StampaRegistroAnnotazioneSpesePGiroBP bp = (StampaRegistroAnnotazioneSpesePGiroBP)BusinessProcess.getBusinessProcess(request); 
   Stampa_registro_annotazione_spese_pgiroBulk bulk = (Stampa_registro_annotazione_spese_pgiroBulk)bp.getModel(); %>

<title> <%= bp.getJSPTitle() %> </title>
<body class="Form">

<% bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
  </tr>

  <% if (bp.isStampa_cnr()) { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdsOrigineForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdCdsOrigineForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsCdsOrigineForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findCdsOrigineForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <% } %>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUnitaOrganizzativaForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdUnitaOrganizzativaForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUnitaOrganizzativaForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUnitaOrganizzativaForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFine"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_obbligazione"); %></td>
	<td colspan=5><% bp.getController().writeFormInput(out,"stato_obbligazione"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.docamm00.docs.bulk.Stampa_elenco_fattureVBulk,
	it.cnr.contab.docamm00.bp.StampaElencoFattureBP"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<% StampaElencoFattureBP bp = (StampaElencoFattureBP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> <%= bp.getJSPTitle() %> </title>
</head>
<body class="Form"> 

<%	Stampa_elenco_fattureVBulk bulk = (Stampa_elenco_fattureVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
</table>
  <br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzo"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_terzo"); %>
		<% bp.getController().writeFormInput(out,"ds_terzo"); %>
		<% bp.getController().writeFormInput(out,"findTerzo"); %>
	</td>
  </tr>
  <tr><td></td></tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdUOForPrint",(bulk!=null?!bulk.isUoForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUoForPrintEnabled():false),null,null); %>
	</td>
  </tr>
</table>
  <br>
<table> 
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_emissione_from"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_emissione_from"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dt_emissione_to"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_emissione_to"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazione_from"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_registrazione_from"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazione_to"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_registrazione_to"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"pgFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_comp_from"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_comp_from"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_comp_to"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_comp_to"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_fine_comp_from"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_fine_comp_from"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dt_fine_comp_to"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_fine_comp_to"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_fattura"); %></td>
	<td colspan="4"><% bp.getController().writeFormInput(out,"esercizio_fattura"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
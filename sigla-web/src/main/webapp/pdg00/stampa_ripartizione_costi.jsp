<%@page import="it.cnr.contab.pdg00.action.StampaRipartizioneCostiAction"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*,
		it.cnr.contab.pdg00.cdip.bulk.Stampa_ripartizione_costiVBulk"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa ripartizione costi su GAE</title>
</head>
<body class="Form">

<%	StampaRipartizioneCostiBP bp = (StampaRipartizioneCostiBP)BusinessProcess.getBusinessProcess(request);
	Stampa_ripartizione_costiVBulk bulk = (Stampa_ripartizione_costiVBulk)bp.getModel(); 
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>	
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdUOForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCommessaForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdCommessaForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsCommessaForPrint"); %>
		<% bp.getController().writeFormInput(out,"findCommessaForPrint"); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findModuloForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdModuloForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsModuloForPrint"); %>
		<% bp.getController().writeFormInput(out,"findModuloForPrint"); %>
	</td>
  </tr>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findDipendenteForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"idDipendenteForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsDipendenteForPrint"); %>
		<% bp.getController().writeFormInput(out,"findDipendenteForPrint"); %>
	</td>
  </tr>    
  <tr>
	<td><% bp.getController().writeFormLabel(out,"mese"); %></td>
	<td><% bp.getController().writeFormInput(out,"mese"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
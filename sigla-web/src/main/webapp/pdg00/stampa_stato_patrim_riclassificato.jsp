<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.pdg00.bulk.Stampa_vpg_stato_patrim_riclassVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa stato patrimoniale riclassificato</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_vpg_stato_patrim_riclassVBulk bulk = (Stampa_vpg_stato_patrim_riclassVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
	<td colspan="5">
		<% bp.getController().writeFormInput(out,null,"cdCDSForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsCDSForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findCDSForPrint",(bulk!=null?!bulk.isCdsForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td colspan="5">
		<% bp.getController().writeFormInput(out,null,"cdUOForPrint",(bulk!=null?!bulk.isUoForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUoForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_ist_com"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_ist_com"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_att_pass"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_att_pass"); %></td>
  </tr> 
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dettaglioConti"); %></td>
	<td><% bp.getController().writeFormInput(out,"dettaglioConti"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
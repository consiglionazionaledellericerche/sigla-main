<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*,
	it.cnr.contab.doccont00.ordine.bulk.Stampa_ordineBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Ordine</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_ordineBulk bulk = (Stampa_ordineBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pgFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgFine"); %></td>
	<td></td>
	<td></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
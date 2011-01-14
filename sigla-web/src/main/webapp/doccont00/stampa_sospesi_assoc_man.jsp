<%@ page 
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.intcass.bulk.Stampa_sospesi_cnr_assoc_manVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa sospesi CNR associati a mandati</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_sospesi_cnr_assoc_manVBulk bulk = (Stampa_sospesi_cnr_assoc_manVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
</table>
<% if (bulk.isCdsEnte()){ %>
<br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdsForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdCdsForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsCdsForPrint"); %>
		<% bp.getController().writeFormInput(out,"findCdsForPrint"); %>
	</td>
  </tr>
</table>
<% } %>
<br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
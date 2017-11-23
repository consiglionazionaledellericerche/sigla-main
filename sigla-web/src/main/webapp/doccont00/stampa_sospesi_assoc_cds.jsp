<%@ page 
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.intcass.bulk.Stampa_sospesi_cnr_assoc_cdsVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa sospesi CNR associati a CdS</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_sospesi_cnr_assoc_cdsVBulk bulk = (Stampa_sospesi_cnr_assoc_cdsVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table class="w-100 card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
<% if (bulk.isCdsEnte()){ %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdsForPrint"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"findCdsForPrint"); %>
	</td>
  </tr>
<% } %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_entrata_spesa"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ti_entrata_spesa"); %></td>
  </tr>
</table>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>
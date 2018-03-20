<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.pdg00.bulk.Stampa_ricostruzione_residui_LAVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Stampa Analitica delle Risorse provenienti dall'esercizio precedente</title>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_ricostruzione_residui_LAVBulk bulk = (Stampa_ricostruzione_residui_LAVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
<table>
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
	  </tr>
	  <tr>	
	  	<td><% bp.getController().writeFormLabel(out,"findCdrForPrint"); %></td>
	  	<td>
	  		<% bp.getController().writeFormInput(out,"cdCdrForPrint"); %>
	  		<% bp.getController().writeFormInput(out,"dsCdrForPrint"); %>
			<% bp.getController().writeFormInput(out,"findCdrForPrint"); %></td>
	  </tr>	
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findDipartimentoForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findDipartimentoForPrint"); %></td>
	  </tr>
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findProgettoForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findProgettoForPrint"); %></td>
	  </tr>	  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findModuloForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findModuloForPrint"); %></td>
	  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
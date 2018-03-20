<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.config00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Elenco Contratti</title>
</head>
<body class="Form"> 

<%	it.cnr.contab.reports.bp.ParametricPrintBP bp = (it.cnr.contab.reports.bp.ParametricPrintBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
<div class="Group">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "findUoForPrint"); %></td>
	<td><% bp.getController().writeFormInput( out, "findUoForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "findTerzo_firmatarioForPrint"); %></td>
	<td><% bp.getController().writeFormInput( out, "findTerzo_firmatarioForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "findFigura_giuridicaForPrint"); %></td>
	<td><% bp.getController().writeFormInput( out, "findFigura_giuridicaForPrint"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "stato"); %></td>
	<td><% bp.getController().writeFormInput( out, "stato"); %></td>
  </tr>
  <tr>  
	<td><% bp.getController().writeFormLabel( out, "findTipo_contrattoForPrint"); %></td>
	<td><% bp.getController().writeFormInput( out, "findTipo_contrattoForPrint"); %></td>
  </tr>
</table>
<div class="Group">
<table>

  <tr>
	<td><% bp.getController().writeFormLabel( out, "DataStipula_da"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataStipula_da"); %></td>
	<td><% bp.getController().writeFormLabel( out, "DataStipula_a"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataStipula_a"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "DataInizioValidita_da"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataInizioValidita_da"); %></td>
	<td><% bp.getController().writeFormLabel( out, "DataInizioValidita_a"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataInizioValidita_a"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "DataFineValidita_da"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataFineValidita_da"); %></td>
	<td><% bp.getController().writeFormLabel( out, "DataFineValidita_a"); %></td>
	<td><% bp.getController().writeFormInput( out, "DataFineValidita_a"); %></td>
  </tr>
  
</table>
</div>
</div>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

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
<title>Stampa Confronto Sigla DWH</title>
</head>
<body class="Form"> 

<%	it.cnr.contab.reports.bp.ParametricPrintBP bp = (it.cnr.contab.reports.bp.ParametricPrintBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
<div class="Group">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "data"); %></td>
	<td><% bp.getController().writeFormInput( out, "data"); %></td>
  </tr>
  
</table>

</div>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
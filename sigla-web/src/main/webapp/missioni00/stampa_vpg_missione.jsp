<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa massiva nota e prospetto liquidazione missione</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %>
		<% bp.getController().writeFormLabel(out,"cd_cds"); %>
		<% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdUOForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdUOForPrint"); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgInizio"); %>
		<% bp.getController().writeFormLabel(out,"pgFine"); %>
		<% bp.getController().writeFormInput(out,"pgFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzo"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cdTerzo"); %>
		<% bp.getController().writeFormInput(out,"findTerzo"); %>
	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
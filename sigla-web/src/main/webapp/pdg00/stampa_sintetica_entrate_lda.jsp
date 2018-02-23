<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.pdg00.bulk.Stampa_sintetica_entrate_ldaBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Sintetica Entrate per LdA</title>
</head>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); 
	Stampa_sintetica_entrate_ldaBulk bulk=(Stampa_sintetica_entrate_ldaBulk)bp.getModel(); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>
</table>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findCdrForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdCdrForPrint",(bulk!=null?!bulk.isCdrForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsCdrForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findCdrForPrint",(bulk!=null?!bulk.isCdrForPrintEnabled():false),null,null); %>
	</td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
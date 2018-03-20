<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Rendiconti Finanziari CNR</title>
</head>
<body class="Form">

<%
	StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel">
    <tr>
		<td colspan=4> </td>
	</tr>

	<tr>
		<td colspan=4><b>Stampa Entrate (importi diversi da zero)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaEntratePerCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaEntratePerArticolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>

	
		<td colspan=4> </td>
	<tr>	
	<tr>
		<td colspan=4><b>Stampa Spese (importi diversi da zero)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesePerCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesePerArticolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>


		<td colspan=4> </td>
	<tr>	
		
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
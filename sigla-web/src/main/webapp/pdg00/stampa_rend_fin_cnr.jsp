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

<table class="Panel card p-2">
	<tr>
		<td colspan=3 class="text-info"><b>Stampa Entrate (importi diversi da zero)</b></td>
	</tr>
	<tr>
		<td>
		    <%JSPUtils.button(out,
		        bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
		        "Per capitolo",
		        "if (disableDblClick()) javascript:submitForm('doStampaEntratePerCapitolo')",
		        "btn-outline-info btn-title",
		        bp.getParentRoot().isBootstrap());%>
		</td>
		<td>
		    <%JSPUtils.button(out,
		        bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
		        "Per articolo",
		        "if (disableDblClick()) javascript:submitForm('doStampaEntratePerArticolo')",
		        "btn-outline-info btn-title",
		        bp.getParentRoot().isBootstrap());%>
		</td>
	</tr>
	<tr>
		<td colspan=3 class="text-primary"><b>Stampa Spese (importi diversi da zero)</b></td>
	</tr>
	<tr>
		<td>
		    <%JSPUtils.button(out,
		        bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
		        "Per capitolo",
		        "if (disableDblClick()) javascript:submitForm('doStampaSpesePerCapitolo')",
		        "btn-outline-primary btn-title",
		        bp.getParentRoot().isBootstrap());%>
		</td>
		<td>
		    <%JSPUtils.button(out,
		        bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
		        "Per articolo",
		        "if (disableDblClick()) javascript:submitForm('doStampaSpesePerArticolo')",
		        "btn-outline-primary btn-title",
		        bp.getParentRoot().isBootstrap());%>
		</td>
		<td></td>
	</tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
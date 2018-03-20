<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% DettagliFileStipendiBP bp = (DettagliFileStipendiBP)BusinessProcess.getBusinessProcess(request); %>

	<title>Flusso Stipendi</title>

<body class="Form">


<% bp.openFormWindow(pageContext); %>

	<table style="width:100%">
		<tr><td>
		<% bp.writeHTMLTable(pageContext,"100%","400px"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>
<%bp.closeFormWindow(pageContext); %>
</body>
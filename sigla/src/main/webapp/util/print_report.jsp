<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.reports.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Anteprima di stampa</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form" style="margin: 0px; overflow: hidden">

<% 	ReportPrintBP bp = (ReportPrintBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); 
	bp.print(pageContext);
	bp.closeFormWindow(pageContext); %>
</body>

</html>
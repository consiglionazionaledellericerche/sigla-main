<!-- 
 ?ResourceName ".jsp"
 ?ResourceTimestamp ""
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<title>Macrolinea di attività</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<% bp.getController().writeForm(out); %>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
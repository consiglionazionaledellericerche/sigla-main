<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Gestione terzo</title>
</head>

<body class="Form">
<%
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<%	bp.closeFormWindow(pageContext); %>
</body>
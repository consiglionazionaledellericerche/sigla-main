<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<%!	static final String[][] tabs = new String[][] {
					{ "tabTestata","Testata","/config00/tab_linea_attivita_testata.jsp" },
					{ "tabRisultati","Risultati","/config00/tab_linea_attivita_risultati.jsp" }};
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Gruppo di Azioni Elementari</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

		<%	JSPUtils.tabbed(
					pageContext,
					"tab",
					tabs,
					bp.getTab("tab"),
					"center",
					"100%",
					"100%" );
		%>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
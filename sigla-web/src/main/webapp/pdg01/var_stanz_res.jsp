<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.varstanz00.bp.*"
%>

<%
CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)BusinessProcess.getBusinessProcess(request);
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getLongDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext); %>
   <% JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   bp.getTabs(session),
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" ); %>
   <% bp.closeFormWindow(pageContext); %>
</body>
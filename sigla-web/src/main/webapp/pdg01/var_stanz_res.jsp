<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<%!     static String[][] tabs = null;
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getLongDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);
   tabs = new String[][] {
	               { "tabTestataVarStanzRes","Testata","/pdg01/tab_var_stanz_res_testata.jsp" },
	               { "tabCDR","CDR abilitati a concorrervi","/pdg01/tab_ass_var_stanz_res_cdr.jsp" }
	               };   
%>
   <% JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   tabs,
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" ); %>
   <% bp.closeFormWindow(pageContext); %>
</body>
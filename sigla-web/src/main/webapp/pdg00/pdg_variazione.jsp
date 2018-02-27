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
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);
   tabs = new String[][] {
	               { "tabTestata","Testata","/pdg00/tab_pdg_variazione_testata.jsp" },
	               { "tabCDR","CDR abilitati a concorrervi","/pdg00/tab_ass_pdg_variazione_cdr.jsp" },
	               { "tabArchivio","Archivio Consultazioni","/pdg00/tab_pdg_variazione_archivio.jsp" },
	               { "tabRiepilogo","Riepilogo per CdR/Dipartimento","/pdg00/tab_pdg_variazione_riepilogo.jsp" }
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
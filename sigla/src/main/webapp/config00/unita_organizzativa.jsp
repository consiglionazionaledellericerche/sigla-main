<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*"
%>
<%
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<%!     static String[][] tabs = null;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Unità Organizzativa</title>
<body class="Form">
<% bp.openFormWindow(pageContext);
   tabs = new String[][] {
	               { "tabUnitaOrganizzativa","Unità Organizzativa","/config00/tab_unita_organizzativa.jsp" },
	               { "tabAreeRicerca","Aree Ricerca Associate","/config00/tab_ass_uo_area.jsp" },
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
</html>

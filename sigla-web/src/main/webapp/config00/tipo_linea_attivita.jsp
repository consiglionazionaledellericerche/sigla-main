<%!	static final String[][] tabs = new String[][] {
					{ "tabTestata","Testata","/config00/tab_tipo_linea_attivita_testata.jsp" },
					{ "tabCdrAssociati","Cdr associati","/config00/tab_tipo_linea_attivita_cdr_associati.jsp" } };
%>

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.bp.*,it.cnr.contab.config00.latt.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<title>GAE comune</title>
<body class="Form">

<% CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)BusinessProcess.getBusinessProcess(request);
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
<%	 bp.closeFormWindow(pageContext); %>
</body>
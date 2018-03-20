<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)BusinessProcess.getBusinessProcess(request);
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
   JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   new String[][]{
                      { "tabTestata","Testata","/prevent01/contrattazione_spese_tes.jsp" },
                      { "tabDettagli","Dettagli","/prevent01/contrattazione_spese_det.jsp" }
   				   },
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" );
   bp.closeFormWindow(pageContext); %>
</body>
</html>
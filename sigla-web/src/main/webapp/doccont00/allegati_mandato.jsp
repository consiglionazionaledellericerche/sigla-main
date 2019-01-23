<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.util00.bp.*"
%>
<%
	AllegatiDocContBP bp = (AllegatiDocContBP)BusinessProcess.getBusinessProcess(request);
%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Allegati al <%=bp.getModel().toString()%></title>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext); %>
<% JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
						{ "tabAllegati","Allegati","/doccont00/tab_allegati_doccont.jsp" },
						{ "tabDocContAlleagti","Doc. Amministrativi collegati","/doccont00/tab_allegati_docamm_mandato.jsp" }
					},
					bp.getTab("tab"),
					"center",
					"100%",
					"100%"); %>
<% bp.closeFormWindow(pageContext); %>
</body>
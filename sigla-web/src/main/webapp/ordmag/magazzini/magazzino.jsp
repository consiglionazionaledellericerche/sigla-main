<%@ page pageEncoding="UTF-8"
    	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request); %>

<title>Gestione Magazzino</title>
</head>
<body class="Form">
<%  bp.openFormWindow(pageContext);
    JSPUtils.tabbed(
				pageContext,
				"tab",
				new String[][] {
					{ "tabMagazzino","Magazzino","/ordmag/magazzini/tab_magazzino.jsp" },
					{ "tabTipoMovimento","Tipo Movimento","/ordmag/magazzini/tab_tipo_movimento.jsp" },
				},
				bp.getTab("tab"),
				"center",
				"100%",
				null );
	%>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
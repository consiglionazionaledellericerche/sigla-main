<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDTipoMovimentoMagBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.ordini.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% CRUDBP bp = (CRUDTipoMovimentoMagBP)BusinessProcess.getBusinessProcess(request); %>

<title>Tipo Movimento Magazzino</title>
</head>
<body class="Form">
<%  bp.openFormWindow(pageContext); %>
	
	<%
	JSPUtils.tabbed(
				pageContext,
				"tab",
				new String[][] {
					{ "tabTipoMovimentoMag","Tipo Movimento","/ordmag/tab_tipo_movimento.jsp" },
					{ "tabTipoMovAzioni","Azioni","/ordmag/tab_tipo_movimento_azioni.jsp" },
				},
				bp.getTab("tab"),
				"center",
				"100%",
				null );
	%>	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
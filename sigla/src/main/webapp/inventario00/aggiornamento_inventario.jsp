<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Aggiornamento Inventario</title>
</head>
<body class="Form">
<% CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); 
	
	JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
				{ "tabAggiornamentoInventarioTestata","Beni","/inventario00/tab_aggiornamento_testata.jsp" },
				{ "tabAggiornamentoInventarioDettaglio","Aggiornamenti","/inventario00/tab_aggiornamento_dettaglio.jsp"},
			},
			bp.getTab("tab"),
			"center",
			"100%",
			null );
		  	
	bp.closeFormWindow(pageContext); %>

</body>
</html>
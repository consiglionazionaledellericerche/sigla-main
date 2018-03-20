<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>

	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<script language="javascript" src="scripts/css.js"></script>
		<script language="JavaScript" src="scripts/util.js"></script>
		<title>Fondo Economale</title>
	</head>

<body class="Form">
	<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext);

		String [][] defaultPages = new String[][] {
					{ "tabFondoEconomale","Testata","/fondecon00/tab_fondo_economale.jsp" },
					{ "tabFondoEconomaleMandati","Mandati associati","/fondecon00/tab_fondo_economale_mandati.jsp" },
					{ "tabFondoEconomaleSpese","Dettagli spese","/fondecon00/tab_fondo_economale_spese.jsp" }
				};

		JSPUtils.tabbed(
						pageContext,
						"tab",
						defaultPages,
						bp.getTab("tab"),
						"center",
						"100%",
						null );

		bp.closeFormWindow(pageContext); %>
</body>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondiric00.bp.*,
		it.cnr.contab.fondiric00.core.bulk.*"
%>

<%!	static final String[][] tabs = new String[][] {
					{ "tabTestata","Testata","/fondiric00/fondi_ricerca_testata.jsp" },
					{ "tabDettagli","Dettagli - assegnatari","/fondiric00/fondi_ricerca_dettagli.jsp" } };
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Fondi di Ricerca</title>
</head>

<body class="Form">
<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table>
	<tr>
		<%bp.getController().writeFormField(out,"cd_anag");%>
	</tr>
</table>

		<%	JSPUtils.tabbed(
					pageContext,
					"tab",
					tabs,
					bp.getTab("tab"),
					"center",
					"100%",
					"100%" );
		%>

<%	bp.closeFormWindow(pageContext); %>
</body>
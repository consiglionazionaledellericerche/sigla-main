<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>RICAVI / ENTRATE</title>
</head>
<body class="Form">

<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext);
	Pdg_aggregato_etr_detBulk prevent_etr = (Pdg_aggregato_etr_detBulk)bp.getModel();
%>
<center><%= prevent_etr.getCdr().getCd_ds_cdr() %></center>
<table class="Panel">
	<tr><td colspan=2>
		<table border="0" cellspacing="0" cellpadding="2">
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
			<tr><% bp.getController().writeFormField(out,"natura");%></tr>
			<tr><% bp.getController().writeFormField(out,"elemento_voce");%></tr>
		</table>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr><td colspan=2>
		<%JSPUtils.tabbed(
				pageContext,
				"tabPreventivoEtr",
				new String[][]{
						{ "tabEsercizio",prevent_etr.getEsercizio().toString(),"/prevent00/tab_dettaglio_etr_prevent.jsp" },
						{ "tabEsercizio2",String.valueOf(prevent_etr.getEsercizio().intValue() + 1),"/prevent00/tab_dettaglio_etr_prevent2.jsp" },
						{ "tabEsercizio3",String.valueOf(prevent_etr.getEsercizio().intValue() + 2),"/prevent00/tab_dettaglio_etr_prevent3.jsp" }
					      },
				bp.getTab("tabPreventivoEtr"),
				"center",
				"600px",
				"150px" );
		%>
	</td></tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
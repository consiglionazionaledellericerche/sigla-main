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
<title>COSTI / SPESE</title>
</head>
<body class="Form">

<% 	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext);
	Pdg_aggregato_spe_detBulk prevent_spe = (Pdg_aggregato_spe_detBulk)bp.getModel();
%>
<center><%= prevent_spe.getCdr().getCd_ds_cdr() %></center>
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
	<tr><center><% bp.getController().writeFormField(out,prevent_spe.getCdr().isCdrSAC() ? "capitolo" : "titolo");%></center></tr>
	<tr><% bp.getController().writeFormField(out,"funzione");%></tr>
	<tr><% bp.getController().writeFormField(out,"natura");%></tr>
	<tr><% bp.getController().writeFormField(out,"cds");%></tr>
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
				"tabPreventivoSpe",
				new String[][]{
						{ "tabEsercizio",String.valueOf(prevent_spe.getEsercizio().intValue()),"/prevent00/tab_dettaglio_spe_prevent.jsp" },
						{ "tabEsercizio2",String.valueOf(prevent_spe.getEsercizio().intValue() + 1),"/prevent00/tab_dettaglio_spe_prevent2.jsp" },
						{ "tabEsercizio3",String.valueOf(prevent_spe.getEsercizio().intValue() + 2),"/prevent00/tab_dettaglio_spe_prevent3.jsp" }
					      },
				bp.getTab("tabPreventivoSpe"),
				"center",
				"600px",
				"300px" );
		%>
	</td></tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
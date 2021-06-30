<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.docs.bulk.*,
	it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Compenso</title>
</head>
<body class="Form"> 

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<div class="Group">
<table class="w-50">
  <tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pgCompensoPos"); %></td>
	<td><% bp.getController().writeFormInput( out, "pgCompensoPos"); %></td>
	
	<% if (compenso.isRiportataInScrivania() && compenso.isLabelRiportoToShow() && !bp.isSearching()) { %>
			<td> &nbsp;&nbsp;&nbsp;&nbsp;
				<span class="FormLabel" style="color:red">
					Documento Riportato
				</span>
			</td>
  	<% } %>	
  </tr>
</table>
</div>
<table class="Panel" width="100%">
  <tr>
	<td><% JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
						{ "tabCompenso","Compenso","/compensi00/tab_compenso.jsp" },
						{ "tabCompensoTerzo","Terzo","/compensi00/tab_compenso_terzo.jsp" },
						{ "tabCompensoDatiLiquidazione","Dati Liquidazione","/compensi00/tab_compenso_dati_liquidazione.jsp" },
						{ "tabCompensoContributiRitenute","Contributi e Ritenute","/compensi00/tab_compenso_contributi_ritentute.jsp" },
						{ "tabCompensoObbligazioni","Impegni","/compensi00/tab_compenso_obbligazioni.jsp" },
						{ "tabCompensoDocumentiAssociati","Documenti Associati","/compensi00/tab_compenso_documenti_associati.jsp" },
						{ "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" },
					},
					bp.getTab("tab"),
					"center",
					"100%",
					null ); %>

	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>
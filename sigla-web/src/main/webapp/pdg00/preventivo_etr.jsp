<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bulk.*,
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>  RICAVI / ENTRATE</title>
</head>
<body class="Form">

<%	CRUDEtrDetPdGBP bp = (CRUDEtrDetPdGBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext);
	Pdg_preventivo_etr_detBulk pdg_etr = (Pdg_preventivo_etr_detBulk)bp.getModel();
%>

<table class="Panel" width="100%" height="100%">
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td><span class="FormLabel">CDR</span></td>
					<td width="100%"><% bp.getController().writeFormInput(out,null,"ds_centro_responsabilita",true,null,"style=\"width:100%\""); %></td>
					<% bp.getController().writeFormField(out,"origine"); %>
				</tr>
				<tr>
					<td><% bp.getController().writeFormLabel(out,"pdg_variazione");%></td>
					<td><% bp.getController().writeFormInput(out,"pdg_variazione");%></td>
				</tr>				
			</table>
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="Group" style="width:100%">
				<table width="100%">
					<tr><% bp.getController().writeFormField(out,"linea_attivita");%></tr>
					<tr><% bp.getController().writeFormField(out,"natura");%></tr>
					<tr><% bp.getController().writeFormField(out,"classificazione_entrate");%></tr>
					<tr><% bp.getController().writeFormField(out,"elemento_voce");%></tr>
				</table>
			</div>
		</td>
	</tr>

	<tr>
		<td>
			<table>
				<tr><% bp.getController().writeFormField(out,"pg_entrata");%></tr>
				<tr><% bp.getController().writeFormField(out,"descrizione");%></tr>
			</table>
		</td>
	</tr>
	<tr height="100%">
		<td>
			<%JSPUtils.tabbed(
					pageContext,
					"tabPreventivoEtr",
					new String[][]{
							{ "tabEsercizio",pdg_etr.getEsercizio().toString(),"/pdg00/tab_dettaglio_etr_pdg.jsp" },
							{ "tabEsercizio2",String.valueOf(pdg_etr.getEsercizio().intValue() + 1),"/pdg00/tab_dettaglio_etr_pdg2.jsp" },
							{ "tabEsercizio3",String.valueOf(pdg_etr.getEsercizio().intValue() + 2),"/pdg00/tab_dettaglio_etr_pdg3.jsp" }
						      },
					bp.getTab("tabPreventivoEtr"),
					"center",
					"100%",
					"100%" );
			%>
		</td>
	</tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
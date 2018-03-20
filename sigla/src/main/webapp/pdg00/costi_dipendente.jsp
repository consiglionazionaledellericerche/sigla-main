<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<%	
	CostiDipendenteBP bp = (CostiDipendenteBP)BusinessProcess.getBusinessProcess(request); 
	V_cdp_matricolaBulk matricola = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% if (bp.getCosti_dipendente().isMensile()) { %>
<title>Ripartizione costi stipendiali mensili</title>
<% } else { %>
<title>Costi del personale</title>
<% } %>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>
<table class="Panel card">
	<tr>
		<td><%	bp.getController().writeFormLabel(out,"unita_organizzativa_filter"); %></td>
		<td><%	bp.getController().writeFormInput(out,null,"unita_organizzativa_filter", bp.isDirty(), null, null); %></td>
	</tr>
</table>
<table class="Panel card">
	<tr>
		<%	if (bp.getCosti_dipendente().isMensile())
				bp.getController().writeFormField(out,"mese"); %>
	</tr>
	<tr>
		<td colspan="2">
			<span class="FormLabel">Costi del personale per matricola</span>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<% bp.getCostiDipendenti().writeHTMLTable(
				pageContext,
				bp.getCosti_dipendente().isMensile()?"mese":null,
				false,
				false,
				false,
				"100%",
				"200px"); %>
		</td>
	</tr>
	<tr>
		<td nowrap>
			<% bp.getCostiDipendenti().writeFormLabel(out,"stato_costo_caricato"); %>
		</td>
		<td width="100%">
			<% bp.getCostiDipendenti().writeFormInput(out,
				null,
				"stato_costo_caricato",
				(matricola!=null && matricola.getCostiScaricati()!=null && !matricola.getCostiScaricati().isEmpty()),
				null,
				"onchange=\"submitForm('doCambiaStatoCostoCaricato')\""); %>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<%JSPUtils.tabbed(
				pageContext,
				"tab",
				new String[][]{
						{ "tabCostiScaricati","Ripartizione per CDR","/pdg00/tab_costi_scaricati.jsp" },
						{ "tabCostiScaricatiAltraUO","Scaricati verso altra UO","/pdg00/tab_costi_scaricati_altra_uo.jsp" }
					      },
				bp.getTab("tab"),
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
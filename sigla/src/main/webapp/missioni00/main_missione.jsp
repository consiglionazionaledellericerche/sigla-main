<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->
 
<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*, it.cnr.jada.action.*, it.cnr.jada.bulk.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Missione</title>
<body class="Form">

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	MissioneBulk missione = (MissioneBulk) bp.getModel();

	if(missione == null)
		missione = new MissioneBulk();	
%>
<div class="card mb-2 p-1">
    <table width="100%">
        <tr>
            <% bp.getController().writeFormField( out, "esercizio"); %>
            <% bp.getController().writeFormField( out, "pg_missione"); %>
        </tr>
        <tr>
            <% bp.getController().writeFormField( out, "cd_cds");%>
            <% bp.getController().writeFormField( out, "cd_unita_organizzativa"); %>
        </tr>
        <% if (missione.isRiportataInScrivania() && missione.isLabelRiportoToShow() && !bp.isSearching()) { %>
        <tr>
                <td colspan="4"><span class="FormLabel text-danger" style="color:red">Documento Riportato</span></td>
        </tr>
        <% } %>
    </table>
</div>
<table class="Panel" width="100%">
	<tr><td>
		<%	
			String[][] pages = null;
			if(missione != null && missione.getPg_missione() != null && missione.getPg_missione().compareTo(new Long (0)) > 0){
				pages = new String[][] {
					{ "tabTestata","Testata","/missioni00/tab_missione_testata.jsp" },							
					{ "tabAnagrafico","Anagrafico","/missioni00/tab_missione_anagrafico.jsp" },
					{ "tabConfigurazioneTappe","Configurazione tappe","/missioni00/tab_missione_configurazione_tappa.jsp" } ,								
					{ "tabDettaglioSpese","Dettaglio spese","/missioni00/tab_missione_dettaglio_spese.jsp" } ,
					{ "tabDettaglioDiaria","Dettaglio diaria","/missioni00/tab_missione_dettaglio_diaria.jsp" } ,
					{ "tabDettaglioRimborso","Dettaglio rimborso","/missioni00/tab_missione_dettaglio_rimborso.jsp" } ,
					{ "tabObbligazione","Documenti associati","/missioni00/tab_missione_obbligazione.jsp" } ,
					{ "tabConsuntivo","Consuntivo","/missioni00/tab_missione_consuntivo.jsp" } ,
					{ "tabAllegati","Allegati","/missioni00/tab_missione_allegati.jsp" }
				};
			} else {
				pages = new String[][] {
					{ "tabTestata","Testata","/missioni00/tab_missione_testata.jsp" },							
					{ "tabAnagrafico","Anagrafico","/missioni00/tab_missione_anagrafico.jsp" },
					{ "tabConfigurazioneTappe","Configurazione tappe","/missioni00/tab_missione_configurazione_tappa.jsp" } ,								
					{ "tabDettaglioSpese","Dettaglio spese","/missioni00/tab_missione_dettaglio_spese.jsp" } ,
					{ "tabDettaglioDiaria","Dettaglio diaria","/missioni00/tab_missione_dettaglio_diaria.jsp" } ,
					{ "tabDettaglioRimborso","Dettaglio rimborso","/missioni00/tab_missione_dettaglio_rimborso.jsp" } ,
					{ "tabObbligazione","Documenti associati","/missioni00/tab_missione_obbligazione.jsp" } ,
					{ "tabConsuntivo","Consuntivo","/missioni00/tab_missione_consuntivo.jsp" } 
				};
			}
	
		
				JSPUtils.tabbed(
						pageContext,
						"tab",
						pages,
						bp.getTab("tab"),
						"center", 
						"100%", 
						null,
						!(bp.isEditingTappa() || bp.getSpesaController().isEditingSpesa()));
		%>
	</td></tr>	
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>
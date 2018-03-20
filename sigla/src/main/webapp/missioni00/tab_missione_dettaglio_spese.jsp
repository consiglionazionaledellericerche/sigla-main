<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) bp.getSpesaController().getModel();

	if(spesa == null)
		spesa = new Missione_dettaglioBulk();	
%>

<table cellpadding="2" width="100%">
	<tr>
		<td>
		     <%	bp.getSpesaController().setEnabled(!bp.getSpesaController().isEditingSpesa());
		      	if((bp.isEditable()) && (!bp.isSearching()))
		      	{
					bp.getSpesaController().writeHTMLTable(pageContext, "dettaglioSpesaSet",true,false,true,"100%","150px");
		      	}
		      	else
		      	{
			      	// Visualizzazione missione
					bp.getSpesaController().writeHTMLTable(pageContext, "dettaglioSpesaSet",false,false,false,"100%","150px");
			    }  		
	      	%>
		</td>
	</tr>
  	<tr>
  		<td colspan=10>
	      <%
	      	String[][] pages = null;
	      	if(spesa != null && spesa.getPg_missione() != null && spesa.getPg_missione().compareTo(new Long (0)) > 0){
	      		pages = new String[][] {
	      			{ "tabDettaglioSpesa","Dettaglio Spesa","/missioni00/tab_missione_dettaglio_spesa.jsp" },
	      			{ "tabDettaglioSpesaAllegati","Allegati","/missioni00/tab_missione_dettaglio_spesa_allegati.jsp" } };
	      	} else {
	      		pages = new String[][] {
	      			{ "tabDettaglioSpesa","Dettaglio Spesa","/missioni00/tab_missione_dettaglio_spesa.jsp" } };
	      	}
	      	JSPUtils.tabbed(pageContext, "tabDettaglioSpese",
	      			pages,
	      			bp.getTab("tabDettaglioSpese"), "left", "100%", null, !bp.getSpesaController().isEditingSpesa());
	      %>
		</td>
	</tr>
</table>
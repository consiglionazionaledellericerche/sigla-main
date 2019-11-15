<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>

<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
		AbilUtenteUopOperBulk riga = (AbilUtenteUopOperBulk)bp.getCrudUtente_abil_ordini().getModel();
		bp.getCrudUtente_abil_ordini().writeHTMLTable(pageContext,"ColumnsAbilOrdini",true,false,true,"100%","140px"); 
%>
<div class="card p-2 mt-2">
<%
	String[][] pages = null;
    pages = new String[][] {
	      			{ "tabAbilOrdiniDettaglio","Dettaglio Abilitazione Ordine","/utenze00/tab_abil_ordine_dettaglio.jsp" },
					{ "tabOrdineConsegna","Consegna","/utenze00/tab_abil_ordine_magazzini.jsp" } };
					
	JSPUtils.tabbed(pageContext, "tabAbilOrdiniDettaglio",
		pages,
		bp.getTab("tabAbilOrdiniDettaglio"), "left", "100%", null, true);
%>			
</div>

<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
%>

<%if( !anagrafico.isStrutturaCNR() ) { %>
	<%	bp.getCrudRapporti().writeHTMLTable(pageContext,null,(!anagrafico.isDipendente() || anagrafico.isAbilitatoTrattamenti()),false,(!anagrafico.isDipendente() || anagrafico.isAbilitatoTrattamenti()),"100%","150px"); %>
	
	<%	JSPUtils.tabbed(
				pageContext,
				"tabRapporti",
				new String[][] {
						{ "tabDettagliRapporto","Dettagli","/anagraf00/tab_dettagli_rapporto.jsp" },
						{ "tabRapportiInquadramento","Inquadramento","/anagraf00/tab_rapporti_inquadramento.jsp" } },
				bp.getTab("tabRapporti"),
				"center",
				"100%",
				null);
	%>
<%}%>
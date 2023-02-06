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

<%	CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
	AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
	RapportoBulk rapporto =(RapportoBulk)bp.getCrudRapporti().getModel();
%>

<%	bp.getCrudInquadramenti().writeHTMLTable(pageContext,null,(!anagrafico.isDipendente()&& rapporto!=null && rapporto.isAbilitato_inquadramento()),false,(!anagrafico.isDipendente()),"100%","auto;max-height:40vh;",false); %>
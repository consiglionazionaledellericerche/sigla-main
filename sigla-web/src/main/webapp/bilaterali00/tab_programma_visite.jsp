<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>
<% 
JSPUtils.tabbed(
		pageContext,
		"tabBltProgrammaVisite",
		new String[][] {
				{ "tabBltProgrammaVisiteIta","Italiani","/bilaterali00/tab_programma_visite_ita.jsp" },
				{ "tabBltProgrammaVisiteStr","Stranieri","/bilaterali00/tab_programma_visite_str.jsp" } },
		bp.getTab("tabBltProgrammaVisite"),
		"center",
		"100%",
		null);
%>

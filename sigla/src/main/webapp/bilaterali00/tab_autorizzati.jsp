<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
%>
<% 
JSPUtils.tabbed(
		pageContext,
		"tabBltAutorizzati",
		new String[][] {
				{ "tabBltAutorizzatiIta","Italiani","/bilaterali00/tab_autorizzati_ita.jsp" },
				{ "tabBltAutorizzatiStr","Stranieri","/bilaterali00/tab_autorizzati_str.jsp" } },
		bp.getTab("tabBltAutorizzati"),
		"center",
		"100%",
		null);
%>

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>

<% bp.getCrudBltAutorizzatiIta().writeHTMLTable(
								pageContext,
								null,
								!bp.isSearching(),
								false,
								!bp.isSearching(),
								"100%",
								"100px");
%>
&nbsp;
<% 
JSPUtils.tabbed(
		pageContext,
		"tabBltAutorizzatiIta",
		new String[][] {
				{ "tabBltAutorizzatiItaGen","Info","/bilaterali00/tab_autorizzati_ita_gen.jsp" },
				{ "tabBltAutorizzatiItaDet","Dettaglio","/bilaterali00/tab_autorizzati_ita_det.jsp" } },
		bp.getTab("tabBltAutorizzatiIta"),
		"center",
		"100%",
		null);
%>
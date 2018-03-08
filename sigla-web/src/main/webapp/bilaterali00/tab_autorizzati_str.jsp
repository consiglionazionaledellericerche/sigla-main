<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>

<% bp.getCrudBltAutorizzatiStr().writeHTMLTable(
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
		"tabBltAutorizzatiStr",
		new String[][] {
				{ "tabBltAutorizzatiStrGen","Info","/bilaterali00/tab_autorizzati_str_gen.jsp" },
				{ "tabBltAutorizzatiStrDet","Dettaglio","/bilaterali00/tab_autorizzati_str_det.jsp" } },
		bp.getTab("tabBltAutorizzatiStr"),
		"center",
		"100%",
		null);
%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.jada.util.jsp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);

	JSPUtils.tabbed(
			pageContext,
			"tabProceduraRapporti",
			bp.getTabsDichiarazioneContraente(),
			bp.getTab("tabProceduraRapporti"),
			"center",
			"99%",
			null);
%>

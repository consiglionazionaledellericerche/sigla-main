<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	        it.cnr.jada.util.jsp.*,
	        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk,
	        it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
	        it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
	CRUDBltVisiteBP bp = (CRUDBltVisiteBP) BusinessProcess
			.getBusinessProcess(request);
	Blt_visiteBulk model = (Blt_visiteBulk) bp.getModel();
%>
<% 
if (model.isAnticipoPrevisto()) {
	if (!model.isVisitaPagataAdEnteStraniero() && model.getFase()>=Blt_visiteBulk.FASE_DODICESIMA) {
		JSPUtils.tabbed(
			pageContext,
			"tabBltVisiteRimborsoSpese",
			new String[][] {
					{ "tabBltVisiteRimborsoSpeseAnticipo","Anticipo","/bilaterali00/tab_rimborso_spese_anticipo.jsp" },
					{ "tabBltVisiteRimborsoSpeseSaldo","Saldo","/bilaterali00/tab_rimborso_spese_saldo.jsp" },
					{ "tabBltVisiteRimborsoSpeseRiepilogo","Riepilogo","/bilaterali00/tab_rimborso_spese_riepilogo.jsp" } },
			bp.getTab("tabBltVisiteRimborsoSpese"),
			"center",
			"100%",
			null);
	} else {
		JSPUtils.tabbed(
				pageContext,
				"tabBltVisiteRimborsoSpese",
				new String[][] {
						{ "tabBltVisiteRimborsoSpeseAnticipo","Anticipo","/bilaterali00/tab_rimborso_spese_anticipo.jsp" },
						{ "tabBltVisiteRimborsoSpeseRiepilogo","Riepilogo","/bilaterali00/tab_rimborso_spese_riepilogo.jsp" } },
				bp.getTab("tabBltVisiteRimborsoSpese"),
				"center",
				"100%",
				null);
	}
} else {
	JSPUtils.tabbed(
			pageContext,
			"tabBltVisiteRimborsoSpese",
			new String[][] {
					{ "tabBltVisiteRimborsoSpeseSaldo","Pagamento","/bilaterali00/tab_rimborso_spese_saldo.jsp" },
					{ "tabBltVisiteRimborsoSpeseRiepilogo","Riepilogo","/bilaterali00/tab_rimborso_spese_riepilogo.jsp" } },
			bp.getTab("tabBltVisiteRimborsoSpese"),
			"center",
			"100%",
			null);
}
%>

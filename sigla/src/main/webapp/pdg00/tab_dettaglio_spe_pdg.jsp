<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<%
	CRUDSpeDetPdGBP bp = (CRUDSpeDetPdGBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2">
	<tr><% bp.getController().writeFormField(out,"im_ru_spese_costi_altrui");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rv_pagamenti");%></tr>
</table>

<%JSPUtils.tabbed(
		pageContext,
		"tabCostiSpese",
		new String[][] {
				{ "tabCostiConSpese", "Costi Con Spese","/pdg00/tab_det_costi_con_spese.jsp" },
				{ "tabCostiSenzaSpese", "Costi Senza Spese","/pdg00/tab_det_costi_senza_spese.jsp" },
				{ "tabSpeseSenzaCosti", "Spese Senza Costi","/pdg00/tab_det_spese_senza_costi.jsp" }
			       },
		bp.getTab("tabCostiSpese"),
		"center",
		"100%",
		"100%");
%>